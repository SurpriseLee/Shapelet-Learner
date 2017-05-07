package tech.artisanhub.ShapeletTrainer;


import org.jfree.util.ArrayUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MergeShapelets {

   public ArrayList<Shapelet> mergeShapelets(ArrayList<Shapelet> shapelets,int requiredClassSize){


       ArrayList<Shapelet> mergedShapeletsOut = new ArrayList<Shapelet>();

       Shapelet currentShapelet = null;
       Iterator<Shapelet> iterator = shapelets.iterator();

       int count = shapelets.size()/requiredClassSize;

       ArrayList<ArrayList<Double>> temp = new ArrayList<ArrayList<Double>>();
       List<Double> currentRow;
       Double classVal;
       int rawSize;
       while (iterator.hasNext()){
           currentShapelet = iterator.next();
           if (count>0){
               currentRow = new ArrayList<Double>(Arrays.asList(currentShapelet.rawContent));
               rawSize = currentRow.size() -1;
               classVal = currentRow.remove(rawSize);
               currentRow.add(rawSize,new Double(currentShapelet.seriesId));
               currentRow.add(rawSize+1,new Double(currentShapelet.startPos));
               currentRow.add(rawSize+2,new Double(classVal));
               temp.add(shapelets.size()/requiredClassSize - count, new ArrayList<Double>(currentRow));
               count --;
           }else {
               count = shapelets.size()/requiredClassSize;
               mergedShapeletsOut.add(new Shapelet(temp));
               temp = new ArrayList<ArrayList<Double>>();
           }

       }
       mergedShapeletsOut.add(new Shapelet(temp));
       return mergedShapeletsOut;
   }
   
   
}
