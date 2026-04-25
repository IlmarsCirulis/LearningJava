package com.ilmarscirulis.linearalgebra.pivotingrules;

import com.ilmarscirulis.structures.Field;

import java.util.ArrayList;

public class FirstNonzeroPivot<T> extends RowPivotingRule<T> {

     public int chooseRow(Field<T> field, ArrayList<ArrayList<T>> matrix, int pivotRow, int pivotColumn) {
        for (int row = pivotRow; row < matrix.size(); row++) {
          if (!matrix.get(row).get(pivotColumn).equals(field.ZERO)) {
              return row;
          }
        }
        return pivotRow;
    }
}
