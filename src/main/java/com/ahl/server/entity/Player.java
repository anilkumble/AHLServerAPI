package com.ahl.server.entity;


import com.ahl.server.AHLConstants;
import com.ahl.server.AHLUtils;
import com.ahl.server.exception.InSufficientDataException;

import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Document(collection = "players")
public class Player {

  @Id
  private ObjectId id;
  private String name;
  private String department;
  private String position;
  private String emailId;
  private int age;
  private int graduatedYear;
  private int phoneNo;
  private List<TeamDetail> teamDetails;

  public Player(String name, String emailId) {
    this.name = name;
    this.emailId = emailId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public int getGraduatedYear() {
    return graduatedYear;
  }

  public void setGraduatedYear(int graduatedYear) {
    this.graduatedYear = graduatedYear;
  }

  public int getPhoneNo() {
    return phoneNo;
  }

  public void setPhoneNo(int phoneNo) {
    this.phoneNo = phoneNo;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public List<TeamDetail> getTeamDetails() {
    return teamDetails;
  }

  public void setTeamDetails(List<TeamDetail> teamDetails) {
    this.teamDetails = teamDetails;
  }

  public static boolean validatePlayer(Player player) throws Exception {

    if (!ObjectUtils.allNotNull(player.getName(),player.getEmailId())
        || ObjectUtils.isEmpty(player.getName())
        || ObjectUtils.isEmpty(player.getEmailId())) {

      Map<String, String> substitueMap = new HashMap<>();
      substitueMap.put("fields", Arrays.toString(new Object[]{AHLConstants.NAME, AHLConstants.EMAIL_ID}));
      throw new InSufficientDataException(AHLConstants.MINIMUM_REQUIRED_FIELDS,substitueMap);
    }
    return AHLUtils.isValidEmailAddress(player.getEmailId());
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Player.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("name='" + name + "'")
        .add("department='" + department + "'")
        .add("position='" + position + "'")
        .add("emailId='" + emailId + "'")
        .add("age=" + age)
        .add("graduatedYear=" + graduatedYear)
        .add("phoneNo=" + phoneNo)
        .add("teamDetails=" + teamDetails)
        .toString();
  }
}


