package util;

public class DummyBulkApi implements Runnable {
	/*
	 * 機器制御並列試験用
	 */
	private SmControlTestUtil util = null;

	private String STR_PARAM = "";


	public DummyBulkApi(String smId) {
		this.util = new SmControlTestUtil();
		switch (smId) {
		case "1":
			this.STR_PARAM = SmConstants.DUMMY_PATTERN_1;
			break;
		case "2":
			this.STR_PARAM = SmConstants.DUMMY_PATTERN_2;
			break;
		case "3":
			this.STR_PARAM = SmConstants.DUMMY_PATTERN_3;
			break;
		case "4":
			this.STR_PARAM = SmConstants.DUMMY_PATTERN_4;
			break;
		case "5":
			this.STR_PARAM = SmConstants.DUMMY_PATTERN_5;
			break;
		case "6":
			this.STR_PARAM = SmConstants.DUMMY_PATTERN_6;
			break;
		default:
			break;
		}
	}



	@Override
	public void run() {
		System.out.println("this.STR_PARAM = " + this.STR_PARAM);
		String output = this.util.callPost(SmConstants.SERVER_URL, this.STR_PARAM);

	}

}