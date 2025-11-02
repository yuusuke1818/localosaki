package jp.co.osaki.sms.deviceCtrl.resultset;

public class MDevRelationJoinTBuildingResultSet {

        private String devId;
        private Long buildingId;
        private String buildingName;

        public MDevRelationJoinTBuildingResultSet() {

        }

        public MDevRelationJoinTBuildingResultSet(Long buildingId, String buildingName) {
            this.buildingId = buildingId;
            this.buildingName = buildingName;
        }

        public String getDevId() {
            return devId;
        }
        public void setDevId(String devId) {
            this.devId = devId;
        }
        public Long getBuildingId() {
            return buildingId;
        }
        public void setBuildingId(Long buildingId) {
            this.buildingId = buildingId;
        }
        public String getBuildingName() {
            return buildingName;
        }
        public void setBuildingName(String buildingName) {
            this.buildingName = buildingName;
        }

    }
