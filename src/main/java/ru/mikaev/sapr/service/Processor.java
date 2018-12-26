package ru.mikaev.sapr.service;

import ru.mikaev.sapr.dto.ConstructionDto;
import ru.mikaev.sapr.dto.ProcessorResult;
import ru.mikaev.sapr.dto.RodDto;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

public class Processor {
    private final List<List<Double>> matrixA;
    private final List<Double> listB;

    public Processor(ConstructionDto construction) {
        final List<RodDto> rods = construction.getRods();

        int matrixASize = rods.size() + 1;

        //matrix with zeros
        matrixA = new ArrayList<>();
        for (int i = 0; i < matrixASize; i++) {
            matrixA.add(new ArrayList<>(matrixASize));
            for (int j = 0; j < matrixASize; j++) {
                matrixA.get(i).add((double) 0);
            }
        }

        int x = 0;
        int y = 0;

        for (int i = 0; i < matrixA.size() - 1; i++) {
            double K = 0;
            try {
                K = (rods.get(i).getE() * rods.get(i).getA()) / rods.get(i).getL();
            } catch (ArithmeticException ex) {
            }

            matrixA.get(x).set(y, matrixA.get(x).get(y) + K);
            matrixA.get(x + 1).set(y, matrixA.get(x + 1).get(y) - K);
            matrixA.get(x).set(y + 1, matrixA.get(x).get(y + 1) - K);
            matrixA.get(x + 1).set(y + 1, matrixA.get(x + 1).get(y + 1) + K);

            x++;
            y++;
        }

        listB = new ArrayList<>();

        List<Double> nodesLoads = new ArrayList<>();

        for (int i = 0; i < rods.size(); i++) {
            RodDto currentRod = rods.get(i);

            nodesLoads.add((double) currentRod.getLeftKnot().getLoad());

            if (i == rods.size() - 1) {
                nodesLoads.add((double) currentRod.getRightKnot().getLoad());
            }
        }

        for (int i = 0; i < matrixA.size(); i++) {
            double qs = 0;
            double qk = 0;

            if (i < matrixA.size() - 1) {
                double qx = rods.get(i).getLoad();
                double L = rods.get(i).getL();

                qs = qx * L / 2;
            }

            if (i > 0) {
                double qx = rods.get(i - 1).getLoad();
                double L = rods.get(i - 1).getL();

                qk = qx * L / 2;
            }

            double B = qs + qk + nodesLoads.get(i);
            listB.add(B);
        }

        if (construction.isSupportLeft()) {
            matrixA.get(0).set(0, (double) 1);
            matrixA.get(0).set(1, (double) 0);

            listB.set(0, (double) 0);
        }

        if (construction.isSupportRight()) {
            int n = matrixA.size() - 1;

            matrixA.get(n).set(n, (double) 1);
            matrixA.get(n).set(n - 1, (double) 0);

            listB.set(n, (double) 0);
        }
    }

    public ProcessorResult process() {
        return new ProcessorResult(getDeltas());
    }

    private List<Double> getDeltas() {
        double mainDelta = detA(matrixA);

        List<Double> listDelta = new ArrayList<>();
        for (int i = 0; i < listB.size(); i++) {
            listDelta.add(detA(newMatrix(matrixA, listB, i)) / mainDelta);
        }

        return listDelta;
    }

    private double detA(List<List<Double>> matrix) {
        if (matrix.size() == 2) {
            return matrix.get(0).get(0) * matrix.get(1).get(1) - matrix.get(0).get(1) * matrix.get(1).get(0);
        } else {
            double rez = 0.0;
            for (int i = 0; i < matrix.size(); i++) {
                rez += matrix.get(0).get(i) * pow(-1, i) * detA(getMinor(matrix, i));
            }
            return rez;
        }
    }

    private List<List<Double>> getMinor(List<List<Double>> matrix, int index) {
        int newMinorSize = matrix.size() - 1;
        List<List<Double>> newMinor = new ArrayList<>();

        for (int i = 0; i < newMinorSize; i++)
            newMinor.add(new ArrayList<>());

        for (int i = 1; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.size(); j++) {
                if (index != j) {
                    final List<Double> doubles = newMinor.get(i - 1);
                    doubles.add(matrix.get(i).get(j));
                }
            }
        }

        return newMinor;
    }

    private List<List<Double>> newMatrix(List<List<Double>> m, List<Double> v, int index) {
        List<List<Double>> matrix = new ArrayList<>();
        for (int i = 0; i < m.size(); i++)
            matrix.add(new ArrayList<>());


        for (int i = 0; i < m.size(); i++) {
            for (int j = 0; j < m.size(); j++) {
                if (j != index)
                    matrix.get(i).add(m.get(i).get(j));
                else
                    matrix.get(i).add(v.get(i));
            }
        }

        return matrix;
    }
}
