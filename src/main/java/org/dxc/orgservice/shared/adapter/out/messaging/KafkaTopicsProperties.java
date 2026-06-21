package org.dxc.orgservice.shared.adapter.out.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka.topics")
public class KafkaTopicsProperties {

    private String countryCreated;
    private String countryUpdated;
    private String countryDeleted;
    private String cityCreated;
    private String cityUpdated;
    private String cityDeleted;
    private String companyCreated;
    private String companyUpdated;
    private String companyDeleted;
    private String campusCreated;
    private String campusUpdated;
    private String campusDeleted;
    private String departmentCreated;
    private String departmentUpdated;
    private String departmentDeleted;
    private String teamCreated;
    private String teamUpdated;
    private String teamDeleted;

    public String countryCreated() { return countryCreated; }
    public String countryUpdated() { return countryUpdated; }
    public String countryDeleted() { return countryDeleted; }
    public String cityCreated() { return cityCreated; }
    public String cityUpdated() { return cityUpdated; }
    public String cityDeleted() { return cityDeleted; }
    public String companyCreated() { return companyCreated; }
    public String companyUpdated() { return companyUpdated; }
    public String companyDeleted() { return companyDeleted; }
    public String campusCreated() { return campusCreated; }
    public String campusUpdated() { return campusUpdated; }
    public String campusDeleted() { return campusDeleted; }
    public String departmentCreated() { return departmentCreated; }
    public String departmentUpdated() { return departmentUpdated; }
    public String departmentDeleted() { return departmentDeleted; }
    public String teamCreated() { return teamCreated; }
    public String teamUpdated() { return teamUpdated; }
    public String teamDeleted() { return teamDeleted; }

    public void setCountryCreated(String v) { this.countryCreated = v; }
    public void setCountryUpdated(String v) { this.countryUpdated = v; }
    public void setCountryDeleted(String v) { this.countryDeleted = v; }
    public void setCityCreated(String v) { this.cityCreated = v; }
    public void setCityUpdated(String v) { this.cityUpdated = v; }
    public void setCityDeleted(String v) { this.cityDeleted = v; }
    public void setCompanyCreated(String v) { this.companyCreated = v; }
    public void setCompanyUpdated(String v) { this.companyUpdated = v; }
    public void setCompanyDeleted(String v) { this.companyDeleted = v; }
    public void setCampusCreated(String v) { this.campusCreated = v; }
    public void setCampusUpdated(String v) { this.campusUpdated = v; }
    public void setCampusDeleted(String v) { this.campusDeleted = v; }
    public void setDepartmentCreated(String v) { this.departmentCreated = v; }
    public void setDepartmentUpdated(String v) { this.departmentUpdated = v; }
    public void setDepartmentDeleted(String v) { this.departmentDeleted = v; }
    public void setTeamCreated(String v) { this.teamCreated = v; }
    public void setTeamUpdated(String v) { this.teamUpdated = v; }
    public void setTeamDeleted(String v) { this.teamDeleted = v; }
}
