package utils;




import interaction.User;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class Route {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Location from; //Поле не может быть null
    private utils.loc.Location to; //Поле может быть null
    private Integer distance; //Поле не может быть null, Значение поля должно быть больше 1

    public Route(int id, String name, double x, Double y, Time creationdate, double from_x, Long from_y, String name_from, int to_x, float to_y, String name_to, Integer distance) {
    }

    public Route(String string, int anInt) {
    }

    public Route(int routeId, String name, ZonedDateTime creationDate, Coordinates coordinates, Location from, utils.loc.Location to, int distance, User user) {
    }

//    public static void builder() {
//    }

    public String getDescription() {
        return id + "," + name +","+ coordinates.getCoorX() + "," + coordinates.getCoorY() + "," + creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy : HH.mm.ss")) + "," + from.getFromX() + "," + from.getFromY() + ","
                + from.getName() + "," + to.getToX() + "," + to.getToY() + "," + to.getName() + "," + distance;
    }

    public Route(String name, double coordinatesX, Double coordinatesY, double fromX, Long fromY, String nameFrom, int toX, float toY, String nameTo, Integer distance ){
        this.id = IdGenerator.nextId();
        this.name = name;
        this.coordinates = new Coordinates(coordinatesX, coordinatesY);
        this.from = new Location(fromX, fromY, nameFrom);
        this.to = new utils.loc.Location(toX, toY, nameTo);
        this.distance = distance;
        this.creationDate = ZonedDateTime.now();
    }

    public Route(RouteInfo information) {
        id = information.id ;
        name = information.name;
        coordinates = new Coordinates(information.x, information.y);
        LocalDateTime date = LocalDateTime.parse(information.creationDate, DateTimeFormatter.ofPattern("dd.MM.yyyy : HH.mm.ss"));
        creationDate = date.atZone(ZoneId.systemDefault());
        from = new Location(information.fromX, information.fromY, information.nameFrom);
        to = new utils.loc.Location(information.toX, information.toY, information.nameTo);
        distance = information.distance;
    }

    public int getId() { return id; }

    public int getDistance(){return distance;}


    @Override
    public String toString(){
        return System.lineSeparator()+
                "id: " + id + System.lineSeparator() +
                "name: " + name + System.lineSeparator() +
                "coordinates: " + coordinates.toString() + System.lineSeparator() +
                "location (from): " + from.toString() + System.lineSeparator() +
                "location (to): " + to.toString() + System.lineSeparator() +
                "distance: " + distance.toString() + System.lineSeparator() +
                "creation date: " + creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy : HH.mm.ss"));
    }

    public void update(RouteInfo routeInfo){
        name = routeInfo.name;
        coordinates = new Coordinates(routeInfo.x, routeInfo.y);
        from = new Location(routeInfo.fromX, routeInfo.fromY, routeInfo.nameFrom);
        to = new utils.loc.Location(routeInfo.toX, routeInfo.toY, routeInfo.nameTo);
        distance = routeInfo.distance;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Location getFrom() {
        return from;
    }

    public utils.loc.Location getTo() {
        return to;
    }
}