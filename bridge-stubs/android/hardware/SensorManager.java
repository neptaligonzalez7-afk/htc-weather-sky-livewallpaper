package android.hardware;

public class SensorManager {
    public static final int SENSOR_DELAY_NORMAL = 3;
    public static final int SENSOR_DELAY_UI = 2;
    public static final int SENSOR_DELAY_GAME = 1;
    public static final int SENSOR_DELAY_FASTEST = 0;
    public Sensor getDefaultSensor(int type) { return null; }
    public boolean registerListener(SensorEventListener listener, Sensor sensor, int samplingPeriodUs) { return false; }
    public void unregisterListener(SensorEventListener listener) {}
    public boolean requestTriggerSensor(TriggerEventListener listener, Sensor sensor) { return false; }
    public boolean cancelTriggerSensor(TriggerEventListener listener, Sensor sensor) { return false; }
}
