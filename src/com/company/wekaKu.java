/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekaproject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.core.Instances;
import static weka.core.Instances.test;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;

/**
 *
 * @author Ghifari
 */
public class wekaKu {
    static Evaluation eval;
    
    public static void saveEval(String path) {
        try{
            // objek file
            Scanner scan = new Scanner(System.in);
            FileWriter berkas = new FileWriter(path);
            
            PrintWriter keluaran = new PrintWriter(berkas);
            
            keluaran.println(eval.toSummaryString("Results\n======\n", false));
            keluaran.println(eval.toClassDetailsString());
            keluaran.println(eval.toMatrixString());
            
            // tutup file
            berkas.close();
            System.out.println("data telah disimpan dengan nama testSimpan.txt");
        }catch(Exception e){
            System.out.println("kesalahan  : "+ e.getMessage());
        }
    }
    
    public static void classify(String trainPath, String testPath) throws Exception {
        // Bagian input data
        DataSource source = new DataSource(trainPath);
        Instances train = source.getDataSet();
        source = new DataSource(testPath);
        Instances test = source.getDataSet();
        
        //Jika nilainya negatif maka tidak class tidak terdefinisi
        if (train.classIndex() == -1){
            train.setClassIndex(train.numAttributes() - 1);
        }
        if (test.classIndex() == -1){
            test.setClassIndex(test.numAttributes() - 1);
        }
        
        //Filtering Train Numeric to Nominal
        NumericToNominal nm = new NumericToNominal();
        String[] options = new String[2];
        options[0] = "-R";
        options[1] = "1-4";
        nm.setOptions(options);
        nm.setInputFormat(train);
        Instances trainedData = Filter.useFilter(train, nm);
        
        //Filtering Test Numeric to Nominal
        NumericToNominal nm2 = new NumericToNominal();
        String[] options2 = new String[2];
        options2[0] = "-R";
        options2[1] = "1-4";
        nm2.setOptions(options);
        nm2.setInputFormat(test);
        Instances testedData = Filter.useFilter(test, nm2);
        
        // train classifier
        Classifier cls = new J48();
        cls.buildClassifier(trainedData);
        
        // evaluate classifier and print some statistics
        eval = new Evaluation(trainedData);
        eval.evaluateModel(cls, trainedData);
        System.out.println(eval.toSummaryString("Results\n======\n", false));
        System.out.println(eval.toClassDetailsString());
        System.out.println(eval.toMatrixString());
        
//        ArffSaver saver =  new ArffSaver();
//        saver.setInstances(testedData);
//        saver.setFile(new File("D:/testedDataIris.arff"));
//        saver.writeBatch();
        System.out.println("===============================");
//        System.out.println(filteredData);
    }
    
    public static void main(String[] args) {
        try {
            String trainPath = "D:/irisTrain.arff";
            String testPath = "D:/irisTest.arff";
            classify(trainPath,testPath);
            saveEval("D:/testSimpanIris.txt");
        }catch (Exception e) {
            System.out.println("eror bos : "+e);
        }
    }
}
