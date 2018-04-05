package com.mlc.exchange.rate.checker;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "currencyFrom")
    private String from;

    @Column(name = "currencyTo")
    private String to;

    @Column(name = "quotationDate")
    private String date;

    private Date lastDateCheck;

    private double rate;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getLastDateCheck() {
        return lastDateCheck;
    }

    public void setLastDateCheck(Date lastDateCheck) {
        this.lastDateCheck = lastDateCheck;
    }
}
