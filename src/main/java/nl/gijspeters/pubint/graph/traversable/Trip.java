package nl.gijspeters.pubint.graph.traversable;

import org.onebusaway.gtfs.model.AgencyAndId;

/**
 * Created by gijspeters on 23-02-17.
 *
 * A class describing a unique bus trip, similar to GTFS AgencyAndId.
 */
public class Trip {

    private String agency;
    private String tripid;

    public Trip() {
    }

    public Trip(AgencyAndId agencyAndId) {
        setAgencyAndId(agencyAndId);
    }

    public AgencyAndId getAgencyAndId() {
        return new AgencyAndId(agency, tripid);
    }

    public void setAgencyAndId(AgencyAndId agencyAndId) {
        agency = agencyAndId.getAgencyId();
        tripid = agencyAndId.getId();
    }

    public int hashCode() {
        return getAgencyAndId().hashCode();
    }

    public boolean equals(Object o) {
        if (o != null && o instanceof Trip) {
            return getAgencyAndId().equals(((Trip) o).getAgencyAndId());
        }
        return false;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getTripid() {
        return tripid;
    }

    public void setTripid(String tripid) {
        this.tripid = tripid;
    }

    public String toString() {
        return "Trip " + agency + ":" + tripid;
    }
}
