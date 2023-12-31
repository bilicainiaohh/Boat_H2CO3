package org.koishi.launcher.h2co3.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.koishi.launcher.h2co3.launcher.H2CO3LauncherActivity;
import org.koishi.launcher.h2co3.launcher.H2CO3LauncherLib;
import org.koishi.launcher.h2co3.launcher.H2CO3LauncherLoader;
import org.koishi.launcher.h2co3.launcher.function.H2CO3LauncherCallback;
import org.koishi.launcher.h2co3.control.client.H2CO3ControlClient;
import org.koishi.launcher.h2co3.control.controller.HardwareController;
import org.koishi.launcher.h2co3.control.controller.VirtualController;
import org.koishi.launcher.h2co3.core.H2CO3Game;
import org.koishi.launcher.h2co3.core.H2CO3Tools;
import org.koishi.launcher.h2co3.core.game.MinecraftVersion;
import org.koishi.launcher.h2co3.core.login.utils.DisplayUtils;
import org.koishi.launcher.h2co3.utils.launch.MCOptionUtils;
import org.koishi.launcher.h2co3.utils.launch.boat.VirGLService;

import java.io.File;
import java.util.Timer;
import java.util.Vector;

public class H2CO3LauncherClientActivity extends H2CO3LauncherActivity implements H2CO3ControlClient {

    private final int[] grabbedPointer = new int[]{999, 89999};
    private boolean grabbed = false;
    private ImageView cursorIcon;
    private final static int CURSOR_SIZE = 16; //dp
    private int screenWidth;
    private int screenHeight;
    MaterialAlertDialogBuilder dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.koishi.launcher.h2co3.launcher.R.layout.overlay);
        mainTextureView = findViewById(org.koishi.launcher.h2co3.launcher.R.id.main_game_render_view);
        baseLayout = findViewById(org.koishi.launcher.h2co3.launcher.R.id.main_base);
        getWindow().getDecorView().findViewById(android.R.id.content).post(() -> {
            screenWidth = getSurfaceLayerView().getWidth();
            screenHeight = getResources().getDisplayMetrics().heightPixels;
        });
        cursorIcon = new ImageView(this);
        cursorIcon.setLayoutParams(new ViewGroup.LayoutParams(DisplayUtils.getPxFromDp(this, CURSOR_SIZE), DisplayUtils.getPxFromDp(this, CURSOR_SIZE)));
        cursorIcon.setImageResource(org.koishi.launcher.h2co3.resources.R.drawable.cursor5);
        this.addView(cursorIcon);
        initUI();
        new Thread(H2CO3LauncherLoader::redirectStdio).start();
    }

    private void initUI() {
        handleCallback();
        init();
        boatInterface.onActivityCreate(this);
        dialog = new MaterialAlertDialogBuilder(H2CO3LauncherClientActivity.this);
    }

    private void handleCallback() {
        setH2CO3LauncherCallback(new H2CO3LauncherCallback() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                configureSurfaceTexture(surface, width, height);
                new Thread(() -> {
                    H2CO3Game gameLaunchSetting = new H2CO3Game();
                    Vector<String> args = H2CO3Game.getMcArgs(gameLaunchSetting, H2CO3LauncherClientActivity.this, (int) (width * scaleFactor), (int) (height * scaleFactor));
                    runOnUiThread(() -> {
                        configureBoatLib(surface);
                        startGame(args);
                    });
                }).start();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            }

            @Override
            public void onCursorModeChange(int mode) {
                if (mode == H2CO3LauncherLib.CursorDisabled) {
                    setGrabCursor(true);
                } else if (mode == H2CO3LauncherLib.CursorEnabled) {
                    setGrabCursor(false);
                }
            }

            @Override
            public void onStart() {
                new Thread(H2CO3LauncherLoader::redirectStdio).start();
            }

            @Override
            public void onPicOutput() {
            }

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onExit(int code) {
                stopVirGLService();
            }
        });
    }

    private void configureSurfaceTexture(SurfaceTexture surface, int width, int height) {
        surface.setDefaultBufferSize((int) (width * scaleFactor), (int) (height * scaleFactor));
        MCOptionUtils.saveOptions(H2CO3Game.getGameDirectory());
        MCOptionUtils.setOption("overrideWidth", String.valueOf((int) (width * scaleFactor)));
        MCOptionUtils.setOption("overrideHeight", String.valueOf((int) (height * scaleFactor)));
        MCOptionUtils.setOption("fullscreen", "true");
        MCOptionUtils.saveOptions(H2CO3Game.getGameDirectory());
    }

    private void configureBoatLib(SurfaceTexture surface) {
        H2CO3LauncherLib.setH2CO3LauncherNativeWindow(new Surface(surface));
        H2CO3LauncherLib.setEventPipe();
    }

    private void startGame(Vector<String> args) {
        String javaPath = H2CO3Game.getJavaPath();
        String boatRenderer = H2CO3Tools.GL_GL114;

        System.out.println(args);
        MinecraftVersion mcVersion = MinecraftVersion.fromDirectory(new File(H2CO3Game.getGameCurrentVersion()));
        boolean isHighVersion = mcVersion.minimumLauncherVersion >= 21;
        startGame(javaPath,
                H2CO3Tools.PUBLIC_FILE_PATH,
                isHighVersion,
                args,
                boatRenderer,
                H2CO3Game.getGameDirectory());
    }

    private void stopVirGLService() {
        Intent virGLService = new Intent(H2CO3LauncherClientActivity.this, VirGLService.class);
        stopService(virGLService);
    }

    @Override
    public void onClick(View p1) {
        // TODO: Implement this method
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setKey(int keyCode, boolean pressed) {
        this.setKey(keyCode, 0, pressed);
    }

    @Override
    public void setPointerInc(int xInc, int yInc) {
        if (!grabbed) {
            int x, y;
            x = grabbedPointer[0] + xInc;
            y = grabbedPointer[1] + yInc;
            if (x >= 0 && x <= screenWidth)
                grabbedPointer[0] += xInc;
            if (y >= 0 && y <= screenHeight)
                grabbedPointer[1] += yInc;
            setPointer(grabbedPointer[0], grabbedPointer[1]);
            this.cursorIcon.post(() -> {
                cursorIcon.setX(grabbedPointer[0]);
                cursorIcon.setY(grabbedPointer[1]);
            });
        } else {
            setPointer(getPointer()[0] + xInc, getPointer()[1] + yInc);
        }
    }

    @Override
    public void setPointer(int x, int y) {
        super.setPointer(x, y);
        if (!grabbed) {
            this.cursorIcon.post(() -> {
                cursorIcon.setX(x);
                cursorIcon.setY(y);
            });
            grabbedPointer[0] = x;
            grabbedPointer[1] = y;
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void addView(View v) {
        this.addContentView(v, v.getLayoutParams());
    }

    @Override
    public void typeWords(String str) {
        if (str == null) return;
        for (int i = 0; i < str.length(); i++) {
            setKey(0, str.charAt(i), true);
            setKey(0, str.charAt(i), false);
        }
    }

    @Override
    public int[] getGrabbedPointer() {
        return this.grabbedPointer;
    }

    @Override
    public int[] getLoosenPointer() {
        return this.getPointer();
    }

    @Override
    public ViewGroup getViewsParent() {
        return (ViewGroup) findViewById(android.R.id.content).getRootView();
    }

    @Override
    public View getSurfaceLayerView() {
        return mainTextureView;
    }

    @Override
    public boolean isGrabbed() {
        return this.grabbed;
    }

    @Override
    public void setGrabCursor(boolean isGrabbed) {
        super.setGrabCursor(isGrabbed);
        this.grabbed = isGrabbed;
        if (!isGrabbed) {
            setPointer(grabbedPointer[0], grabbedPointer[1]);
            cursorIcon.post(() -> cursorIcon.setVisibility(View.VISIBLE));
        } else if (cursorIcon.getVisibility() == View.VISIBLE) {
            cursorIcon.post(() -> cursorIcon.setVisibility(View.INVISIBLE));
        }
    }

    public static void attachControllerInterface() {
        H2CO3LauncherClientActivity.boatInterface = new H2CO3LauncherClientActivity.IBoat() {
            private VirtualController virtualController;
            private HardwareController hardwareController;
            private Timer timer;

            @Override
            public void onActivityCreate(H2CO3LauncherActivity boatActivity) {
                virtualController = new VirtualController((H2CO3ControlClient) boatActivity, KEYMAP_TO_X);
                hardwareController = new HardwareController((H2CO3ControlClient) boatActivity, KEYMAP_TO_X);
            }

            @Override
            public void setGrabCursor(boolean isGrabbed) {
                virtualController.setGrabCursor(isGrabbed);
                hardwareController.setGrabCursor(isGrabbed);
            }

            @Override
            public void onStop() {
                virtualController.onStop();
                hardwareController.onStop();
            }

            @Override
            public void onResume() {
                virtualController.onResumed();
                hardwareController.onResumed();
            }

            @Override
            public void onPause() {
                virtualController.onPaused();
                hardwareController.onPaused();
            }

            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                return hardwareController.dispatchKeyEvent(event);
            }

            @Override
            public boolean dispatchGenericMotionEvent(MotionEvent event) {
                return hardwareController.dispatchMotionKeyEvent(event);
            }
        };
    }
}