package com.codeup.blog.blog.models;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "int(11) UNSIGNED")
    private long id;

    private String title;


    private boolean active;

    @Column(columnDefinition = "TEXT")
    private String post_url;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String interview_attendants;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(columnDefinition = "TEXT")
    private String timeline;

    @Column(name="applied_date", columnDefinition="DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date applied_date;

    @Column(name="notified_date", columnDefinition="DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date notified_date;

    @Column(name="interview_date", columnDefinition="DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date interview_date;

    @Column(name="thank_you_sent", columnDefinition="DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date thank_you_sent;

    @Column(name="timestamp", columnDefinition="DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date time_stamp;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private com.codeup.blog.blog.models.Company company;

    public JobPost() {

    }

    public JobPost(String title, boolean active, String post_url, String description, String interview_attendants, String notes, String timeline, Date applied_date, Date notified_date, Date interview_date, Date thank_you_sent, Date time_stamp, User user, Company company) {
        this.title = title;
        this.active = active;
        this.post_url = post_url;
        this.description = description;
        this.interview_attendants = interview_attendants;
        this.notes = notes;
        this.timeline = timeline;
        this.applied_date = applied_date;
        this.notified_date = notified_date;
        this.interview_date = interview_date;
        this.thank_you_sent = thank_you_sent;
        this.time_stamp = time_stamp;
        this.user = user;
        this.company = company;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public com.codeup.blog.blog.models.User getUser() {
        return user;
    }

    public void setUser(com.codeup.blog.blog.models.User user) {
        this.user = user;
    }

    public Date getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Date time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getTime_stamp_String() {
        String pattern = "MMMM dd,yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(time_stamp);
        return date;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInterview_attendants() {
        return interview_attendants;
    }

    public void setInterview_attendants(String interview_attendants) {
        this.interview_attendants = interview_attendants;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getApplied_date() {
        return applied_date;
    }

    public void setApplied_date(Date applied_date) {
        this.applied_date = applied_date;
    }

    public Date getNotified_date() {
        return notified_date;
    }

    public void setNotified_date(Date notified_date) {
        this.notified_date = notified_date;
    }

    public Date getInterview_date() {
        return interview_date;
    }

    public void setInterview_date(Date interview_date) {
        this.interview_date = interview_date;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getPost_url() {
        return post_url;
    }

    public void setPost_url(String post_url) {
        this.post_url = post_url;
    }

    public Date getThank_you_sent() {
        return thank_you_sent;
    }

    public void setThank_you_sent(Date thank_you_sent) {
        this.thank_you_sent = thank_you_sent;
    }
}

