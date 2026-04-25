package com.ilmarscirulis.linearalgebra.pivotingrules;

import com.ilmarscirulis.structures.Field;

import java.util.ArrayList;

abstract public class RowPivotingRule<T> {
    public abstract int chooseRow(Field<T> field, ArrayList<ArrayList<T>> matrix, int pivotRow, int pivotColumn);
}
