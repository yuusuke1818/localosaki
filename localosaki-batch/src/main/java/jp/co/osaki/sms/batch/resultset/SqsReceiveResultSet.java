package jp.co.osaki.sms.batch.resultset;

public class SqsReceiveResultSet {
	private String device_id;
	private String manage_number;
	private String point_value;
	private String meter_value;
	private String serial_id;
	private String meter_reading_time;
	private String alarm_code;

	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getManage_number() {
		return manage_number;
	}
	public void setManage_number(String manage_number) {
		this.manage_number = manage_number;
	}
	public String getPoint_value() {
		return point_value;
	}
	public void setPoint_value(String point_value) {
		this.point_value = point_value;
	}
	public String getMeter_value() {
		return meter_value;
	}
	public void setMeter_value(String meter_value) {
		this.meter_value = meter_value;
	}
	public String getSerial_id() {
		return serial_id;
	}
	public void setSerial_id(String serial_id) {
		this.serial_id = serial_id;
	}
	public String getMeter_reading_time() {
		return meter_reading_time;
	}
	public void setMeter_reading_time(String meter_reading_time) {
		this.meter_reading_time = meter_reading_time;
	}
	public String getAlarm_code() {
		return alarm_code;
	}
	public void setAlarm_code(String alarm_code) {
		this.alarm_code = alarm_code;
	}



}
