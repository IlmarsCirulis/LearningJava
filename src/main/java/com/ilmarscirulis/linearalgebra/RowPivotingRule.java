package com.ilmarscirulis.linearalgebra;

import com.ilmarscirulis.structures.Field;

import java.util.ArrayList;

abstract public class RowPivotingRule<T> {
    abstract int chooseRow(Field<T> field, ArrayList<ArrayList<T>> matrix, int pivotRow, int pivotColumn);
}
