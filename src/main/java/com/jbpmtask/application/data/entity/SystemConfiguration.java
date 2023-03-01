package com.jbpmtask.application.data.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "system_configuration")
public class SystemConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "days_per_week")
    private String daysPerWeek;

    @Column(name = "hours_per_day")
    private String hoursPerDay;

    @Column(name = "start_hour")
    private String startHour;

    @Column(name = "end_hour")
    private String endHour;

    @Column(name = "holidays")
    private String holidays;

    @Column(name = "holiday_date_format")
    private String holidayDateFormat;

    @Column(name = "weekend_days")
    private String weekendDays;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "constant_date")
    private LocalDate constantDate;

    public LocalDate getConstantDate() {
        return constantDate;
    }

    public void setConstantDate(LocalDate constantDate) {
        this.constantDate = constantDate;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getWeekendDays() {
        return weekendDays;
    }

    public void setWeekendDays(String weekendDays) {
        this.weekendDays = weekendDays;
    }

    public String getHolidayDateFormat() {
        return holidayDateFormat;
    }

    public void setHolidayDateFormat(String holidayDateFormat) {
        this.holidayDateFormat = holidayDateFormat;
    }

    public String getHolidays() {
        return holidays;
    }

    public void setHolidays(String holidays) {
        this.holidays = holidays;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getHoursPerDay() {
        return hoursPerDay;
    }

    public void setHoursPerDay(String hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
    }

    public String getDaysPerWeek() {
        return daysPerWeek;
    }

    public void setDaysPerWeek(String daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}