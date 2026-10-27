package android.hardware;

public class SensorEvent {
    public float[] values = new float[3];
    public Sensor sensor;
    public int accuracy;
    public long timestamp;
}
