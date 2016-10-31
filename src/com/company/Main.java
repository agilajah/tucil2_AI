package com.company;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;

import java.util.Scanner;
import java.io.File;



public class Main {

    public static void main(String[] args) throws Exception{

        //create the scanner
        Scanner terminalInput = new Scanner(System.in);
        System.out.println("Masukkan nama file : ");
        //read filename
        String namafile = terminalInput.nextLine();

        DataSource source = new DataSource("/home/agilajah/IdeaProjects/tucil2_AI/dataset/" + namafile + "-copy.arff");
        Instances dataset = source.getDataSet();
        dataset.setClassIndex(dataset.numAttributes() - 1);

        //set options
        String[] options = new String[4];
        //choose the number of intervals, e.g 2:
        options[0] = "-B";
        options[1] = "5";
        //choose the range of attributes on which to apply the filter:
        options[2] = "-R";
        options[3] = "first-last";

        //Apply Discretization
        Discretize discretize = new Discretize();
        discretize.setOptions(options);
        discretize.setInputFormat(dataset);
        Instances newData = Filter.useFilter(dataset, discretize);

        System.out.println(dataset.toSummaryString());

        ArffSaver saver =  new ArffSaver();
        saver.setInstances(newData);
        saver.setFile(new File("/home/agilajah/IdeaProjects/tucil2_AI/dataset/" + namafile + "-copy2.arff"));
        saver.writeBatch();

    }
}
