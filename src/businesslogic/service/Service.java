package businesslogic.service;

import businesslogic.summarySheet.SummarySheet;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;

public class Service {

    private int ID;
    private String name;
    private String proposed_menu_id;
    private String proposed_approved_menu_id;
    private Date service_date;
    private Time time_start;
    private Time time_end;
    private int expected_partecipants;

    public Service (int id) { this.ID = id; }


    public static Service getById(int id) {
        String query = "SELECT * FROM services WHERE id=" + id + ";";

        final String[] getName = new String[1];
        final String[] getMenu = new String[1];
        final String[] getappMenu = new String[1];
        final Date[] getDate = new Date[1];
        final Time[] getTSt = new Time[1];
        final Time[] getTEnd = new Time[1];
        final int[] getNPrt = new int[1];


        PersistenceManager.executeQuery(query, new ResultHandler() {


            @Override
            public SummarySheet handle(ResultSet rs) throws SQLException {
                getName[0] = rs.getString("name");
                getMenu[0] = rs.getString("proposed_menu_id");
                getappMenu[0] = rs.getString("approved_menu_id");
                getDate[0] = rs.getDate("service_date");
                getTSt[0] = rs.getTime("time_start");
                getTEnd[0] = rs.getTime("time_end");
                getNPrt[0] = rs.getInt("expected_participants");

                return null;
            }
        });



        Service svc = new Service(id);

        svc.setName(getName[0]);
        svc.setProposed_menu_id(getMenu[0]);
        svc.setProposed_approved_menu_id(getappMenu[0]);
        svc.setService_date(getDate[0]);
        svc.setTime_start(getTSt[0]);
        svc.setTime_end(getTEnd[0]);
        svc.setExpected_partecipants(getNPrt[0]);

        return svc;
    }

    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProposed_menu_id() { return proposed_menu_id; }
    public void setProposed_menu_id(String proposed_menu_id) { this.proposed_menu_id = proposed_menu_id; }

    public String getProposed_approved_menu_id() { return proposed_approved_menu_id; }
    public void setProposed_approved_menu_id(String proposed_approved_menu_id) { this.proposed_approved_menu_id = proposed_approved_menu_id; }

    public Date getService_date() { return service_date; }
    public void setService_date(Date service_date) { this.service_date = service_date; }

    public Time getTime_start() { return time_start; }
    public void setTime_start(Time time_start) { this.time_start = time_start; }

    public Time getTime_end() { return time_end; }
    public void setTime_end(Time time_end) { this.time_end = time_end; }

    public int getExpected_partecipants() { return expected_partecipants; }
    public void setExpected_partecipants(int expected_partecipants) { this.expected_partecipants = expected_partecipants; }
}