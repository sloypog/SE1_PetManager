package ch.hsr.ifs.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import ch.hsr.ifs.persistence.ActiveRecord;
import ch.hsr.ifs.persistence.ActiveRecordManager;


public class Pet implements ActiveRecord {
	
	private int id = NOTINDB;
	private Person owner;
	private int ownerID;
	private String name;
	private String breed;
	private Date birthday;
	
	public Pet(String name, String breed) {
		this.name = name;
		this.breed = breed;
		this.birthday = new Date();
	}
	
	public int getAge() {
		return 5;
	}
	
	
	public Pet(ResultSet row) throws SQLException {
		this(row.getString("name"), row.getString("breed"));
		this.id = row.getInt("id");
		this.ownerID = row.getInt("owner");
	}
	
	public Boolean equals(Pet otherPet) {
		return name.equals(otherPet.name) &&
				birthday.equals(otherPet.birthday) && breed.equals(otherPet.breed);
	}
	
	public void setOwner(Person owner) {
		this.owner = owner;
		ownerID = owner.getID();
	}
	
	public Person getOwner(){
		return owner;
	}
	
	public int getOwnerID() {
		return ownerID;
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public boolean isInDB() {
		return id > NOTINDB;
	}

	public boolean save() {
		if(owner == null || !owner.isInDB())
			return false;
		try {
			if (!isInDB()) {
				id = ActiveRecordManager.executeInsert("insert into pet (name,breed,owner) values (?,?,?)", name, breed, Integer.toString(owner.getID()));
			} else {
				ActiveRecordManager.execute("UPDATE pet SET owner = ?, name = ?, breed = ? WHERE id = ?", Integer.toString(owner.getID()), name,breed, Integer.toString(id));
			}
		} catch (SQLException e) {
			System.err.println(e);
			return false;
		}
		return true;
	}
	
	public boolean delete() {
		try {
			if (isInDB()) {
				ActiveRecordManager.execute("DELETE FROM pet WHERE id = ?", Integer.toString(id));
				List<Integer> lastIdList = ActiveRecordManager.getIntegerList("SELECT MAX(id) FROM pet;");
				ActiveRecordManager.execute("UPDATE sqlite_sequence SET seq=? WHERE name='pet';", lastIdList.get(0).toString());
			}
		} catch (SQLException e) {
			return false;
		}
		return true;	
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pet) {
			Pet otherPet = (Pet) obj;
			
			if(isInDB()){
				return id == otherPet.getID();
			}
			else{
				return name.equals(otherPet.name) && breed.equals(otherPet.breed);
			}
		}
		return false;
	}
	
	public static List<Pet> findAll() {
		String sql = "SELECT * FROM pet;";
		return ActiveRecordManager.getObjectList(sql, Pet.class);
	}
	
	public static Pet findByID(int id) {
		String sql = "select * from pet WHERE id = " + id + ";";
		return ActiveRecordManager.getObjectList(sql,Pet.class).get(0);
	}
	
	public static List<Pet> findByOwner(Person owner) {
		String sql = "select * from pet WHERE owner = " + owner.getID() + ";";
		return ActiveRecordManager.getObjectList(sql,Pet.class);
		
	}
	@Override
	public String toString() {
		return "Tier ID: " + id + " Name: " + name + " Rasse: " + breed;
	}

	public void loadOwner() {
		owner = Person.findByID(ownerID);
	}
	
}
