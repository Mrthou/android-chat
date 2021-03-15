package com.ntu.treatment;

public class PatientHistory {
    private String username;
    private String patientKind;
    private String patientCondition;
    private String myDoctor;
    private String submitTime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPatientKind() {
        return patientKind;
    }

    public void setPatientKind(String patientKind) {
        this.patientKind = patientKind;
    }

    public String getPatientCondition() {
        return patientCondition;
    }

    public void setPatientCondition(String patientCondition) {
        this.patientCondition = patientCondition;
    }

    public String getMyDoctor() {
        return myDoctor;
    }

    public void setMyDoctor(String myDoctor) {
        this.myDoctor = myDoctor;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    @Override
    public String toString() {
        return "PatientHistory{" +
                "username='" + username + '\'' +
                ", patientKind='" + patientKind + '\'' +
                ", patientCondition='" + patientCondition + '\'' +
                ", myDoctor='" + myDoctor + '\'' +
                ", submitTime='" + submitTime + '\'' +
                '}';
    }
}
