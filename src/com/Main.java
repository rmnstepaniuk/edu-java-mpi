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

import mpi.MPI;


public class Main {
    public static final int n = 1000;
    public static int[][] MR;
    public static int[][] MX;
    public static int[][] MZ;
    public static int[] B;
    public static int[] Z;
    public static int e;

    public static void main(String[] args) {

        // Initialize MPI
        MPI.Init(args);

        // Rank and Size
        int size = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();

        if (rank == 0) {
            System.out.print("Starting main process...");
            System.out.println("Number of processes: " + size);
        }
        switch (rank) {
            case 0:
                T1(rank, size);
            case 1:
                T2(rank, size);
                break;
            case 2:
                T3(rank, size);
                break;
            case 3:
                T4(rank, size);
                break;
            default:
                break;
        }
        // Finalize MPI
        MPI.Finalize();
    }

    public static void T1(int rank, int size) {

        int h = n / size;

        int beginning = rank * h;
        int end = (rank + 1) * h;

        MR = InputVM.matrixInput(n);

        int[] B0 = new int[n];
        int[] Z0 = new int[n];
        int[][] MX0 = new int[n][n];
        int[][] MZ0 = new int[n][n];

        for (int i = 0; i < n; i++) {
            MPI.COMM_WORLD.Send(MR[i], 0, n, MPI.INT, 1, 101);
            MPI.COMM_WORLD.Send(MR[i], 0, n, MPI.INT, 2, 102);
            MPI.COMM_WORLD.Send(MR[i], 0, n, MPI.INT, 3, 103);
        }

        MPI.COMM_WORLD.Recv(B0, 0, n, MPI.INT, 1, 110);
        System.out.println("Process " + rank + " received B");
        MPI.COMM_WORLD.Recv(Z0, 0, n, MPI.INT, 1, 210);
        System.out.println("Process " + rank + " received Z");

        for (int i = 0; i < n; i++) {
            MPI.COMM_WORLD.Recv(MX0[i], 0, n, MPI.INT, 2, 120);
        }
        System.out.println("Process " + rank + " received MX");

        for (int i = 0; i < n; i++) {
            MPI.COMM_WORLD.Recv(MZ0[i], 0, n, MPI.INT, 3, 130);
        }
        System.out.println("Process " + rank + " received MZ");

        // Обчислення MAh
        int[][] MA0 = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = beginning; j < end; j++) {
                for (int k = 0; k < n; k++) {
                    MA0[i][j] = MR[i][k] * MX0[k][j];
                }
            }
        }

        // Обчислення Ch
        int[] C0 = new int[n];

        for (int i = beginning; i < end; i++) {
            for (int j = 0; j < n; j++) {
                C0[i] += B0[j] * MZ0[i][j];
            }
        }

        //Обчислення Ah
        int[] A0 = new int[n];

        for (int i = beginning; i < end; i++) {
            for (int j = 0; j < n; j++) {
                A0[i] += Z0[j] * MA0[i][j];
            }
        }

        // Обчислення a
        int a0 = 0;
        for (int i = beginning; i < end; i++) {
            a0 += B0[i] * Z0[i];
        }

        MPI.COMM_WORLD.Send(a0, 0, 1, MPI.INT, 3, 203);

        MPI.COMM_WORLD.Recv(a0, 0, 1, MPI.INT, 3, 230);

        // Обчислення Dh
        int[] D0 = new int[n];
        for (int i = beginning; i < end; i++) {
            D0[i] = A0[i] - C0[i];
        }

        // Обчислення bi
        int b0;
        b0 = Integer.MIN_VALUE;
        for (int i = beginning; i < end; i++) {
            if (D0[i] > b0) {
                b0 = D0[i];
            }
        }

        MPI.COMM_WORLD.Send(b0, 0, 1, MPI.INT, 3, 303);

        MPI.COMM_WORLD.Recv(b0, 0, 1, MPI.INT, 3, 330);

        // Обчислення e
        int e0;
        e0 = b0 + a0;

        MPI.COMM_WORLD.Send(e0, 0, 1, MPI.INT, 3, 403);

        System.out.println("Finishing process " + rank);

    }

    public static void T2(int rank, int size) {

        int h = n / size;

        int beginning = rank * h;
        int end = (rank + 1) * h;

        B = InputVM.vectorInput(n);
        Z = InputVM.vectorInput(n);

        int[][] MR1 = new int[n][n];
        int[][] MX1 = new int[n][n];
        int[][] MZ1 = new int[n][n];

        for (int i = 0; i < n; i++) {
            MPI.COMM_WORLD.Recv(MR1[i], 0, n, MPI.INT, 0, 101);
        }
        System.out.println("Process " + rank + " received MR");

        MPI.COMM_WORLD.Send(B, 0, n, MPI.INT, 0, 110);
        MPI.COMM_WORLD.Send(B, 0, n, MPI.INT, 2, 112);
        MPI.COMM_WORLD.Send(B, 0, n, MPI.INT, 3, 113);

        MPI.COMM_WORLD.Send(Z, 0, n, MPI.INT, 0, 210);
        MPI.COMM_WORLD.Send(Z, 0, n, MPI.INT, 2, 212);
        MPI.COMM_WORLD.Send(Z, 0, n, MPI.INT, 3, 213);

        for (int i = 0; i < n; i++) {
            MPI.COMM_WORLD.Recv(MX1[i], 0, n, MPI.INT, 2, 121);
        }
        System.out.println("Process " + rank + " received MX");

        for (int i = 0; i < n; i++) {
            MPI.COMM_WORLD.Recv(MZ1[i], 0, n, MPI.INT, 3, 131);
        }
        System.out.println("Process " + rank + " received MZ");

        // Обчислення MAh
        int[][] MA1 = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = beginning; j < end; j++) {
                for (int k = 0; k < n; k++) {
                    MA1[i][j] = MR1[i][k] * MX1[k][j];
                }
            }
        }

        // Обчислення Ch
        int[] C1 = new int[n];

        for (int i = beginning; i < end; i++) {
            for (int j = 0; j < n; j++) {
                C1[i] += B[j] * MZ1[i][j];
            }
        }

        //Обчислення Ah
        int[] A1 = new int[n];

        for (int i = beginning; i < end; i++) {
            for (int j = 0; j < n; j++) {
                A1[i] += Z[j] * MA1[i][j];
            }
        }

        // Обчислення a
        int a1;
        a1 = 0;
        for (int i = beginning; i < end; i++) {
            a1 += B[i] * Z[i];
        }

        MPI.COMM_WORLD.Send(a1, 0, 1, MPI.INT, 3, 313);

        MPI.COMM_WORLD.Recv(a1, 0, 1, MPI.INT, 3, 231);

        // Обчислення Dh
        int[] D1 = new int[n];
        for (int i = beginning; i < end; i++) {
            D1[i] = A1[i] - C1[i];
        }

        // Обчислення bi
        int b1;
        b1 = Integer.MIN_VALUE;
        for (int i = beginning; i < end; i++) {
            if (D1[i] > b1) {
                b1 = D1[i];
            }
        }

        MPI.COMM_WORLD.Send(b1, 0, 1, MPI.INT, 3, 413);

        MPI.COMM_WORLD.Recv(b1, 0, 1, MPI.INT, 3, 331);

        // Обчислення e
        int e1;
        e1 = b1 + a1;

        MPI.COMM_WORLD.Send(e1, 0, 1, MPI.INT, 3, 513);

        System.out.println("Finishing process " + rank);
    }

    public static void T3(int rank, int size) {

        int h = n / size;

        int beginning = rank * h;
        int end = (rank + 1) * h;


        MX = InputVM.matrixInput(n);

        int[][] MR2 = new int[n][n];
        int[] B2 = new int[n];
        int[] Z2 = new int[n];
        int[][] MZ2 = new int[n][n];

        for (int i = 0; i < n; i++) {
            MPI.COMM_WORLD.Recv(MR2[i], 0, n, MPI.INT, 0, 102);
        }
        System.out.println("Process " + rank + " received MR");

        MPI.COMM_WORLD.Recv(B2, 0, n, MPI.INT, 1, 112);
        System.out.println("Process " + rank + " received B");
        MPI.COMM_WORLD.Recv(Z2, 0, n, MPI.INT, 1, 212);
        System.out.println("Process " + rank + " received Z");

        for (int i = 0; i < n; i++) {
            MPI.COMM_WORLD.Send(MX[i], 0, n, MPI.INT, 0, 120);
            MPI.COMM_WORLD.Send(MX[i], 0, n, MPI.INT, 1, 121);
            MPI.COMM_WORLD.Send(MX[i], 0, n, MPI.INT, 3, 123);
        }

        for (int i = 0; i < n; i++) {
            MPI.COMM_WORLD.Recv(MZ2[i], 0, n, MPI.INT, 3, 132);
        }
        System.out.println("Process " + rank + " received MZ");

        // Обчислення MAh
        int[][] MA2 = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = beginning; j < end; j++) {
                for (int k = 0; k < n; k++) {
                    MA2[i][j] = MR2[i][k] * MX[k][j];
                }
            }
        }

        // Обчислення Ch
        int[] C2 = new int[n];

        for (int i = beginning; i < end; i++) {
            for (int j = 0; j < n; j++) {
                C2[i] += B[j] * MZ2[i][j];
            }
        }

        //Обчислення Ah
        int[] A2 = new int[n];

        for (int i = beginning; i < end; i++) {
            for (int j = 0; j < n; j++) {
                A2[i] += Z2[j] * MA2[i][j];
            }
        }

        // Обчислення a
        int a2;
        a2 = 0;
        for (int i = beginning; i < end; i++) {
            a2 += B2[i] * Z2[i];
        }

        MPI.COMM_WORLD.Send(a2, 0, 1, MPI.INT, 3, 223);

        MPI.COMM_WORLD.Recv(a2, 0, 1, MPI.INT, 3, 232);

        // Обчислення Dh
        int[] D2 = new int[n];
        for (int i = beginning; i < end; i++) {
            D2[i] = A2[i] - C2[i];
        }

        // Обчислення bi
        int b2;
        b2 = Integer.MIN_VALUE;
        for (int i = beginning; i < end; i++) {
            if (D2[i] > b2) {
                b2 = D2[i];
            }
        }

        MPI.COMM_WORLD.Send(b2, 0, 1, MPI.INT, 3, 323);

        MPI.COMM_WORLD.Recv(b2, 0, 1, MPI.INT, 3, 332);

        // Обчислення e
        int e2;
        e2 = b2 + a2;

        MPI.COMM_WORLD.Send(e2, 0, 1, MPI.INT, 3, 423);

        System.out.println("Finishing process " + rank);

    }

    public static void T4(int rank, int size) {

        int h = n / size;

        int beginning = rank * h;
        int end = (rank + 1) * h;

        int[][] MR3 = new int[n][n];
        int[] B3 = new int[n];
        int[] Z3 = new int[n];
        int[][] MX3 = new int[n][n];

        MZ = InputVM.matrixInput(n);

        for (int i = 0; i < n; i++) {
            MPI.COMM_WORLD.Recv(MR3[i], 0, n, MPI.INT, 0, 103);
        }
        System.out.println("Process " + rank + " received MR");

        MPI.COMM_WORLD.Recv(B3, 0, n, MPI.INT, 1, 113);
        System.out.println("Process " + rank + " received B");
        MPI.COMM_WORLD.Recv(Z3, 0, n, MPI.INT, 1, 213);
        System.out.println("Process " + rank + " received Z");

        for (int i = 0; i < n; i++) {
            MPI.COMM_WORLD.Recv(MX3[i], 0, n, MPI.INT, 2, 123);
        }
        System.out.println("Process " + rank + " received MX");

        for (int i = 0; i < n; i++) {
            MPI.COMM_WORLD.Send(MZ[i], 0, n, MPI.INT, 0, 130);
            MPI.COMM_WORLD.Send(MZ[i], 0, n, MPI.INT, 1, 131);
            MPI.COMM_WORLD.Send(MZ[i], 0, n, MPI.INT, 2, 132);
        }

        // Обчислення MAh
        int[][] MA3 = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = beginning; j < end; j++) {
                for (int k = 0; k < n; k++) {
                    MA3[i][j] = MR3[i][k] * MX3[k][j];
                }
            }
        }

        // Обчислення Ch
        int[] C3 = new int[n];

        for (int i = beginning; i < end; i++) {
            for (int j = 0; j < n; j++) {
                C3[i] += B3[j] * MZ[i][j];
            }
        }

        //Обчислення Ah
        int[] A3 = new int[n];

        for (int i = beginning; i < end; i++) {
            for (int j = 0; j < n; j++) {
                A3[i] += Z3[j] * MA3[i][j];
            }
        }

        // Обчислення ah
        int a0 = 0;
        int a1 = 0;
        int a2 = 0;
        int a3;
        a3 = 0;
        for (int i = beginning; i < end; i++) {
            a3 += B3[i] * Z3[i];
        }

        MPI.COMM_WORLD.Recv(a0, 0, 1, MPI.INT, 0, 203);
        MPI.COMM_WORLD.Recv(a1, 0, 1, MPI.INT, 1, 213);
        MPI.COMM_WORLD.Recv(a2, 0, 1, MPI.INT, 2, 223);

        int a;
        a = a0 + a1 + a2 + a3;

        MPI.COMM_WORLD.Send(a, 0, 1, MPI.INT, 0, 230);
        MPI.COMM_WORLD.Send(a, 0, 1, MPI.INT, 1, 231);
        MPI.COMM_WORLD.Send(a, 0, 1, MPI.INT, 2, 232);

        // Обчислення Dh
        int[] D3 = new int[n];
        for (int i = beginning; i < end; i++) {
            D3[i] = A3[i] - C3[i];
        }

        // Обчислення bi
        int b0 = 0;
        int b1 = 0;
        int b2 = 0;
        int b3 = 0;
        b3 = Integer.MIN_VALUE;
        for (int i = beginning; i < end; i++) {
            if (D3[i] > b3) {
                b3 = D3[i];
            }
        }

        MPI.COMM_WORLD.Recv(b0, 0, 1, MPI.INT, 0, 303);
        MPI.COMM_WORLD.Recv(b1, 0, 1, MPI.INT, 1, 413);
        MPI.COMM_WORLD.Recv(b2, 0, 1, MPI.INT, 2, 323);

        int b;
        b = b0 + b1 + b2 + b3;

        MPI.COMM_WORLD.Send(b, 0, 1, MPI.INT, 0, 330);
        MPI.COMM_WORLD.Send(b, 0, 1, MPI.INT, 2, 331);
        MPI.COMM_WORLD.Send(b, 0, 1, MPI.INT, 2, 332);

        // Обчислення e
        int e;
        e = b + a;

        MPI.COMM_WORLD.Recv(e, 0, 1, MPI.INT, 0, 403);
        MPI.COMM_WORLD.Recv(e, 0, 1, MPI.INT, 1, 513);
        MPI.COMM_WORLD.Recv(e, 0, 1, MPI.INT, 2, 423);

        System.out.println("e = " + e);

        System.out.println("Finishing process " + rank);
    }
}
