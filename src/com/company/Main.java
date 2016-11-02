/* Author : Febi Agil Ifdillah (febiagil20@gmail.com)
            Ahmad Farhan Ghifari

   Informatics/Computer Science
   Institut Teknologi Bandung (2016)
 */

package com.company;


import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;
import weka.classifiers.trees.J48;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;

import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

import static com.company.Main.saveModel;


public class Main {

    static Instances dataSet;
    static Instances newDataSet;
    static Evaluation usedEvaluation;
    static J48 tree;

    public static Instances applyFilter(Instances dataSet) throws Exception {
        //set options
        String[] optionsFilter = new String[4];
        //choose the number of intervals, e.g 2:
        optionsFilter[0] = "-B";
        optionsFilter[1] = "5";
        //choose the range of attributes on which to apply the filter:
        optionsFilter[2] = "-R";
        optionsFilter[3] = "first-last";

        //Apply Discretization
        Discretize discretize = new Discretize();
        discretize.setOptions(optionsFilter);
        discretize.setInputFormat(dataSet);
        Instances newDataTemp = Filter.useFilter(dataSet, discretize);
        return newDataTemp;
    }

    public static void baseClassifier() throws Exception {
        String[] optionsClassifier = new String[4];
        optionsClassifier[0] = "-C";
        optionsClassifier[1] = "0.3";
        optionsClassifier[2] = "-M";
        optionsClassifier[3] = "3";
        tree.setOptions(optionsClassifier);
        tree.buildClassifier(newDataSet);
    }

    public static Evaluation full_training(Instances newDataSet, Classifier c) throws Exception {
        Evaluation ft = new Evaluation(newDataSet);
        ft.evaluateModel(c, newDataSet);
        return ft;
    }

    public static Evaluation crossValidation(Instances newDataSet, Classifier c) throws  Exception {
        int seed = 1;
        int folds = 10;

        Random rand = new Random(seed);

        //create random dataset
        Instances randData = new Instances(newDataSet);
        randData.randomize(rand);

        Evaluation eval = new Evaluation(randData);
        eval.crossValidateModel(tree, randData, folds, rand);

        return eval;
    }

    public static void saveModel(String filename) throws Exception {
        weka.core.SerializationHelper.write("/home/agilajah/IdeaProjects/tucil2_AI/out/" + filename + ".model", tree);
    }


    public static Classifier loadModel(String filename) throws Exception {
        Classifier loader = (J48) weka.core.SerializationHelper.read("/home/agilajah/IdeaProjects/tucil2_AI/out/" + filename + ".model");

        return loader;
    }

    public static void loadData() throws Exception {
        newDataSet = dataSet;
    }

    public static void filterInteractive() throws Exception {
        //create the scanner
        Scanner terminalInput = new Scanner(System.in);

        System.out.println("Want to use filter? (Yes/No)");
        String chooseFilter = terminalInput.nextLine();
        String tempChoose = chooseFilter.toLowerCase();
        if (tempChoose.equals("yes") || tempChoose.equals("y")) {
            System.out.println("Filter activated.");
            newDataSet = applyFilter(dataSet);
        } else {
            newDataSet = dataSet;
            System.out.println("Filter not activated.");
        }
        // make a class from last attribute
        if (newDataSet.classIndex() == -1) {
            newDataSet.setClassIndex(newDataSet.numAttributes() - 1);
        }
    }


    public static int trainingMethodInteractive() {
        //create the scanner
        Scanner terminalInput = new Scanner(System.in);

        System.out.println();
        System.out.println("Choose appropriate training method : ");
        System.out.println("1. 10 Folds Cross Validation");
        System.out.println("2. Full-Training");
        String chooseMethods = terminalInput.nextLine();
        while (!chooseMethods.equals("1") && !chooseMethods.equals("2")) {
            chooseMethods = terminalInput.nextLine();
            if (!chooseMethods.equals("1") && !chooseMethods.equals("2")) {
                System.out.println("Choose correctly, please.");
            }
        }

        if (chooseMethods.equals("1")) {
            return 1;
        } else {
            if (chooseMethods.equals("2")) {
                return 2;
            }
        }

        //default
        return 1;

    }

    public static void modelInteractive() throws Exception {
        //create the scanner
        Scanner terminalInput = new Scanner(System.in);


        // check whether user has model or not
        System.out.println("Wanna input your model? (Yes/No)");
        String modelName = terminalInput.nextLine();
        String tempChoose = modelName.toLowerCase();
        if (tempChoose.equals("yes") || tempChoose.equals("y")) {
            System.out.println();
            System.out.println("----------------------------------------------------------------");
            System.out.println("|                       Model Loader                            ");
            System.out.println("----------------------------------------------------------------");
            System.out.println();
            System.out.print("Input your model filename : ");
            String modelFileName = terminalInput.nextLine();
            tree = (J48) loadModel(modelFileName);
            System.out.println("Model succesfully loaded.");
            System.out.println(tree);
            loadData();
            System.out.println("Dataset loaded.");
            if (trainingMethodInteractive() == 1) {
                usedEvaluation = crossValidation(newDataSet, tree);
            } else
                usedEvaluation = full_training(newDataSet, tree);

        } else { // if user has no existing model
            System.out.println();
            loadData();
            System.out.println("Dataset loaded.");
            //building classifier model
            baseClassifier();
            System.out.println("----------------------------------------------------------------");
            System.out.println("|                       Model Builder                           ");
            System.out.println("----------------------------------------------------------------");
            System.out.println();
            System.out.println("Model successfully built.");
            System.out.println();
            System.out.println("Want to save model? (Yes/No)");

            String chooseSaveModel = terminalInput.nextLine();
            tempChoose = chooseSaveModel.toLowerCase();
            if (tempChoose.equals("yes") || tempChoose.equals("y")) {
                System.out.println();
                System.out.print("Input your filename here: ");
                String filename = terminalInput.nextLine();
                saveModel(filename);
                System.out.println("----------------------------------------------------------------");
                System.out.println("                          Model Saved                           ");
                System.out.println("----------------------------------------------------------------");
            } else {
                System.out.println("----------------------------------------------------------------");
                System.out.println("|                       Model not Saved                         ");
                System.out.println("----------------------------------------------------------------");
            }

            System.out.println(tree);
            //filterInteractive();
            if (trainingMethodInteractive() == 1) {
                usedEvaluation = crossValidation(newDataSet, tree);
            } else
                usedEvaluation = full_training(newDataSet, tree);

        }


    }//end of modelinteractive


    public static void addNewInstance() throws Exception {
        //create the scanner
        Scanner terminalInput = new Scanner(System.in);
        System.out.println("Write down your new instance : ");

        ArrayList<Attribute> atts = new ArrayList<Attribute>();
        ArrayList<String> classVal = new ArrayList<String>();
        classVal.add("Iris-setosa");
        classVal.add("Iris-versicolor");
        classVal.add("Iris-virginica");

        atts.add(new Attribute("sepallength"));
        atts.add(new Attribute("sepalwidth"));
        atts.add(new Attribute("petallength"));
        atts.add(new Attribute("petalwidth"));

        atts.add(new Attribute("@@class@@",classVal));

        double[] attValues = new double[dataSet.numAttributes()];
        for (int i=0; i < dataSet.numAttributes()-1;i++) {
            attValues[i] = terminalInput.nextDouble();
        }

        Instance instance1 = new DenseInstance(1.0, attValues);
        System.out.println(instance1);
        instance1.setDataset(newDataSet);
        int classify1 = (int) tree.classifyInstance(instance1);
        System.out.print("Prediction Class : ");
        System.out.println(classVal.get(classify1));
    }

    public static void main(String[] args) throws Exception {

        //create the scanner
        Scanner terminalInput = new Scanner(System.in);

        System.out.println("Input dataset file name: ");
        //read filename
        String namafile = terminalInput.nextLine();

        System.out.println("Pilih menu: ");
        System.out.println("1. Gunakan Filter");
        System.out.println("2. Training - Uji");
        //read menu
        String menu = terminalInput.nextLine();
        String temp_menu = menu.toLowerCase();


        DataSource source = new DataSource("/home/agilajah/IdeaProjects/tucil2_AI/dataset/" + namafile + ".arff");
        dataSet = source.getDataSet();
        // make a class from last attribute
        if (dataSet.classIndex() == -1) {
            dataSet.setClassIndex(dataSet.numAttributes() - 1);
        }

        // create base classifier
        tree = new J48();


        if (temp_menu.equals("1")) {
            filterInteractive();
            System.out.println(newDataSet.toSummaryString());
        } else {
            // asking for existing model
            modelInteractive();
            System.out.println(usedEvaluation.toClassDetailsString());
            System.out.println();
            System.out.println();
            System.out.println("Wanna test with new instance? (Yes/No)");
            String yesno = terminalInput.nextLine();
            String tempyesno = yesno.toLowerCase();
            if (yesno.equals("yes")) {
                addNewInstance();
            }


        }



    }
}
