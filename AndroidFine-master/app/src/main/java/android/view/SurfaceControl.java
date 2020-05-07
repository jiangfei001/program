
package android.view;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * SurfaceControl
 *
 * @hide
 */
public class SurfaceControl implements Parcelable {

    protected SurfaceControl(Parcel in) {
    }

    public static final Creator<SurfaceControl> CREATOR = new Creator<SurfaceControl>() {
        @Override
        public SurfaceControl createFromParcel(Parcel in) {
            return new SurfaceControl(in);
        }

        @Override
        public SurfaceControl[] newArray(int size) {
            return new SurfaceControl[size];
        }
    };

    public static Bitmap screenshot(int i, int i1) {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
