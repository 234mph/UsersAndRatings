package com.example.model;

public class Rating {
    private int sum;    // Total sum of all ratings
    private int count;  // Total number of reviews
    private double average;  // Average rating

    public Rating() {
    }

    public Rating(int sum, int count, double average) {
        this.sum = sum;
        this.count = count;
        this.average = average;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "sum=" + sum +
                ", count=" + count +
                ", average=" + average +
                '}';
    }
}