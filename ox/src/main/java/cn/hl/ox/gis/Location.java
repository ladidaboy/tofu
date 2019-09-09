package cn.hl.ox.gis;

/**
 * @author hyman
 * @date 2019-08-08 13:02:22
 * @version $ Id: Location.java, v 0.1  hyman Exp $
 */
public class Location {
    /**GIS格式化地址*/
    public String formattedAddress;
    /**地址*/
    public String address = "*";
    /**经度*/
    public double lng;
    /**纬度*/
    public double lat;

    public Location(String address) {
        this.address = address;
    }

    public Location(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "Location{" + "address='" + address + "'" + ", formattedAddress='" + formattedAddress + "' lng=" + lng + ", lat=" + lat + '}';
    }
}
