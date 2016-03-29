package appsrox.example.accelerometer;


public class Particle {
	/* coefficient of restitution */
	private static final float COR = 0.7f;
	
    public float mPosX;
    public float mPosY;
    private float mVelX;
    private float mVelY;
    
    public void updatePosition(float sx, float sy, float sz, long timestamp) {
    	float dt = (System.nanoTime() - timestamp) / 1000000000.0f;
    	//mVelX += -sx/ 10000000000.0f * dt;
    	//mVelY += -sy/ 10000000000.0f * dt;
    	mPosX += -sx/ 5000.0f * dt;
    	mPosY += -sy/ 10000.0f * dt;
    }
    
    public void resolveCollisionWithBounds(float mHorizontalBound, float mVerticalBound) {
        if (mPosX > mHorizontalBound) {
            mPosX = mHorizontalBound;
            mVelX = -mVelX * COR;
        } else if (mPosX < -mHorizontalBound) {
            mPosX = -mHorizontalBound;
            mVelX = -mVelX * COR;
        }
        if (mPosY > mVerticalBound) {
            mPosY = mVerticalBound;
            mVelY = -mVelY * COR;
        } else if (mPosY < -mVerticalBound) {
            mPosY = -mVerticalBound;
            mVelY = -mVelY * COR;
        }
    }
}
