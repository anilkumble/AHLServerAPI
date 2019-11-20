package com.ahl.server.entity;


import com.ahl.server.AHLConstants;
import com.ahl.server.AHLUtils;
import com.ahl.server.enums.Position;
import com.ahl.server.exception.InSufficientDataException;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Document(collection = "players")
public class Player {

    @Id
    private ObjectId id;
    private String name;
    private String department;
    private Position position;
    private String emailId;
    private int age;
    private int graduatedYear;
    private String phoneNo;
    private int profile;

    public Player(String name, String emailId) {
        this.name = name;
        this.emailId = emailId;
    }

    public ObjectId getId() {
        return id;
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public static void validatePlayer(Player player) throws Exception {
        if (!ObjectUtils.allNotNull(player.getName(), player.getPosition())
                || ObjectUtils.isEmpty(player.getName()) || ObjectUtils.isEmpty(player.getPosition())) {

            Map<String, String> substitueMap = new HashMap<>();
            substitueMap.put("fields", Arrays.toString(new Object[]{AHLConstants.NAME, AHLConstants.POSITION}));
            throw new InSufficientDataException(AHLConstants.MINIMUM_REQUIRED_FIELDS, substitueMap);
        }
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
                .toString();
    }
}


