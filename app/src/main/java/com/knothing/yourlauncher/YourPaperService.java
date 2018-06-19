package com.knothing.yourlauncher;

import android.graphics.Canvas;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.service.wallpaper.WallpaperService;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class YourPaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new YourWallPaperEngine();
    }

    class YourWallPaperEngine extends Engine{

        private MediaPlayer mediaPlayer = null;

        private Camera camera = null;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
//            init();
            initCamera();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if(mediaPlayer != null){
                if(visible){
                    mediaPlayer.start();
                }else{
                    mediaPlayer.pause();
                }
            }
        }


        float startX,startY;
        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            switch (event.getAction()){

                case MotionEvent.ACTION_DOWN:
                    startX = event.getRawX();
                    startY = event.getRawY();
                    handleCanvas();
                    break;

                case MotionEvent.ACTION_UP:
                    break;

                case MotionEvent.ACTION_MOVE:
                    break;

                case MotionEvent.ACTION_CANCEL:
                    break;

            }
        }

        private void handleCanvas() {
            Canvas canvas = null;
            try{
                canvas = getSurfaceHolder().lockCanvas();
                if(canvas != null){
                    canvas.drawColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(canvas != null){
                    getSurfaceHolder().unlockCanvasAndPost(canvas);
                }
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
//            release();
            releaseCamera();
        }

        private void init() {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(mediaPlayer -> {

            });
            mediaPlayer.setOnPreparedListener(listener ->{
                mediaPlayer.start();
            });
            try{
                mediaPlayer.setDisplay(getSurfaceHolder());
//                mediaPlayer.setDataSource(YourPaperService.this,file);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepareAsync();
            }catch (Exception e){
                e.printStackTrace();
                release();
            }
        }

        private void release() {
            if(mediaPlayer != null){
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }

        private void initCamera() {
            try{
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                camera.setDisplayOrientation(90);
                camera.setPreviewDisplay(getSurfaceHolder());
                camera.setPreviewCallback((bytes, camera) -> {

                });
                camera.startPreview();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private void releaseCamera() {
            if(camera != null){
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        }
    }
}
