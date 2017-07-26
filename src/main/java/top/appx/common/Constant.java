package top.appx.common;

public class Constant {
    public enum  JobStatus{
        normal(1),//正常
        pause(2);//暂停

        private int value;

        private JobStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
