/**
 * Паралельне програмування
 * Розрахункова графічна робота
 * Розділ 2
 * @author Степанюк Роман
 * ІВ-91
 * Варіант 25
 *
 * e = max(Z * (MR * MX) - B * MZ) + (B * Z)
 */

package com;

import java.util.Random;

public class InputVM {
    static Random random = new Random();

    /**
     * метод для вводу даних вектора
     * @param length - кількість елементів вектора
     * @return int[] - сформований вектор
     */
    public static int[] vectorInput(int length) {
        int[] vector = new int[length];
        for (int i = 0; i < vector.length ; i++){
            if (length > 100) {
                vector[i] = 1;
            }
            else {
                vector[i] = random.nextInt(100);
            }
        }
        return vector;
    }
    /**
     * метод для вводу даних матриці
     * @param length - розмірність матриці
     * @return int[][] - сформована матриця
     */
    public static int[][] matrixInput(int length) {
        int[][] matrix = new int[length][length];
        for (int i = 0; i < length ; i++){
            for (int j = 0; j < length ; j++){
                if (length > 100) {
                    matrix[i][j] = 1;
                }
                else {
                    matrix[i][j] = random.nextInt(100);
                }
            }
        }
        return matrix;
    }
}
