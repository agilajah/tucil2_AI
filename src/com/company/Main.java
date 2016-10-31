package com.company;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;


import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;



public class Main {

    public static void main(String[] args) throws Exception{
	    //DataSource source = new DataSource("/dataset/something.arff");
        //Instances data = source.getDataSet();
        //if (data.classIndex() == -1)
        //    data.setClassIndex(data.numAttributes() - 1);

        //create the scanner
        Scanner terminalInput = new Scanner(System.in);
        System.out.println("Masukkan nama file : ");
        //read filename
        String namafile = terminalInput.nextLine();
        Instances dataset = new Instances(new BufferedReader(new FileReader("/home/agilajah/IdeaProjects/iristucil2_AI/dataset/" + namafile + ".arff")));


        System.out.println(dataset.toSummaryString());

        ArffSaver saver =  new ArffSaver();
        saver.setInstances(dataset);
        saver.setFile(new File("/home/agilajah/IdeaProjects/tucil2_AI/dataset/" + namafile + "-copy.arff"));
        saver.writeBatch();

    }
}
