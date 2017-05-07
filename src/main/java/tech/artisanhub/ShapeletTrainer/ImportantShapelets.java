package tech.artisanhub.ShapeletTrainer;

import weka.core.Instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jawadhsr on 8/14/16.
 */
public class ImportantShapelets {

    public ArrayList<Shapelet> GetImportantShapelets(ArrayList<Shapelet> shapelets, Instances dataSet,ArrayList<Integer> classValues) {
        ArrayList<Shapelet> shapeletsArr = new ArrayList<Shapelet>();
        ArrayList<Double> classValProbs = new ArrayList<Double>();
        Map<Integer, ShapeletBucket> shapeletBucket = new HashMap<Integer, ShapeletBucket>();
        System.out.println();
        for (int i = 0; i < classValues.size(); i++) {
            ShapeletBucket temp = new ShapeletBucket(classValues.get(i));

            classValProbs.add(findProb(dataSet, classValues.get(i)));
            shapeletBucket.put(classValues.get(i), temp);
            //remember Above can be optimized.
        }
        Map<Integer,Double> clasNprob = new HashMap<Integer, Double>(); 

        for (Shapelet s : shapelets) {
            // Size of MaxProbClassVal is always one as it returns only a key value pair.
            //where key is the class value and value is the probability.
            for(Integer val : MaxProbClassVal(s).keySet()) {

                clasNprob.put(val, MaxProbClassVal(s).get(val));
                // clas = Integer.parseInt(clasNprob.keySet().toArray()[0].toString());
                shapeletBucket.get(val).put(s);
            }

        }
        Map <Integer,Map<Shapelet,Double>> shapeDiff = new HashMap<Integer, Map<Shapelet, Double>>();
        int i=0;
        for (int clas : classValues) {
            Map<Shapelet,Double> temp = new HashMap<Shapelet, Double>();
            double aVal = classValProbs.get(i);
            i++;
            for (Shapelet s : shapeletBucket.get(clas).getShapeletSet()) {

                double val = clasNprob.get(clas);
                temp.put(s, val - aVal);
                //differences[clas][s.seriesId] = Math.abs(/*Here the prob(class Val) of shapelt has to be included. */-classValProbs.get(clas));
                // this has to be changed. The above is wrong.
            }
            shapeDiff.put(clas,temp);

            Map<Shapelet,Double> tmp = shapeDiff.get(clas);
            // Done. Now Test the values.
            Shapelet newShape = GetMinDifShape(tmp);
            // Have to change this heavily.
            shapeletsArr.add(newShape);
        }


        return shapeletsArr;

    }

    private Shapelet GetMinDifShape(Map<Shapelet,Double> shapeDiffs) {
        Shapelet shapelet = null; // = new Shapelet(minDiffs);
        Double minVal = Double.MAX_VALUE;
        for(Map.Entry<Shapelet,Double> sd : shapeDiffs.entrySet()){
            if(minVal > sd.getValue()){
                minVal = sd.getValue();
                shapelet = sd.getKey();
            }
        }

        return shapelet;
    }


    // calculate the maximum class label probability of each shapelet
    private Map<Integer,Double> MaxProbClassVal(Shapelet shaplet) {
       ArrayList<ArrayList<Double>> shapeContent = shaplet.contentInMergedShapelets;
        ArrayList<Double> tempArr = new ArrayList<Double>();   // store class label of the each shapelet of each merged shapelet group
        int count = 0;
        for(ArrayList<Double> val : shapeContent){
            tempArr.add(count,val.get(val.size()-1));
            count ++;
        }

        Map <Integer,Double> retValue = new HashMap<Integer, Double>();
        //Arrays.sort(tempArr);
        Map <Double,Integer> classValCount = new HashMap<Double, Integer>();   // count the number of each class label

        for(int i=0;i<tempArr.size();i++){
           if(classValCount.containsKey(tempArr.get(i))){
               Integer val = classValCount.get(tempArr.get(i));
               val ++;
               classValCount.put(tempArr.get(i),val);
           }
            else {
               classValCount.put(tempArr.get(i),1);
           }
        }
        int MaxVal = 0;
        Double MaxKey = 0.0; // classValues
       for (Map.Entry<Double,Integer> val : classValCount.entrySet()){    // go through all the element of a Map
            if(val.getValue()>MaxVal){
                MaxVal = val.getValue();
                MaxKey = val.getKey();
            }
       }
        Double maxProb = MaxVal*1.0/tempArr.size();
        retValue.put(MaxKey.intValue(),maxProb);
        return retValue;
    }

    private double findProb(Instances data, int classVal) {
        int count = 0;
        for (int i = 0; i < data.size(); i++) {
            if ((int)(data.get(i).classValue()) == classVal) {
                count++;
            }
        }

        double var =  count*1.0/data.size();
        System.out.println("Probability of the classvalue "+classVal+" is "+ var );
        return var;

    }



}
