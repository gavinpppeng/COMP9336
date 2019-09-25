package com.example.assignment;

public class Goertzel {

    private float samplingRate;
    private float targetFrequency;
    private int n;

    private double coeff, Q1, Q2;
    private double sine, cosine;

    public Goertzel(float samplingRate, int inN) {
        this.samplingRate = samplingRate;
        this.n = inN;
    }

    public void resetGoertzel() {
        Q1 = 0;
        Q2 = 0;
    }

    public void initGoertzel(float targetFrequency) {
        int k;
        float floatN;
        double omega;

        floatN = (float) n;
        k = (int) (0.5 + ((floatN * targetFrequency) / samplingRate));
        omega = (2.0 * Math.PI * k) / floatN;
        sine = Math.sin(omega);
        cosine = Math.cos(omega);
        coeff = 2.0 * cosine;

        resetGoertzel();
    }

    public void processSample(double sample) {
        double Q0;

        Q0 = coeff * Q1 - Q2 + sample;
        Q2 = Q1;
        Q1 = Q0;
    }

    public double getMagnitudeSquared() {
        return (Q1 * Q1 + Q2 * Q2 - Q1 * Q2 * coeff);
    }
}
