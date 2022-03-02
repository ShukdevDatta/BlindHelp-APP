/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.andoirdActivity.tflite;

import android.app.Fragment;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;

import org.tensorflow.lite.examples.classification.customview.AutoFitTextureView;
import org.tensorflow.lite.examples.classification.env.Logger;

import java.util.concurrent.Semaphore;

public class CameraConnectionFragment extends Fragment {
  private static final Logger LOGGER = new Logger();

  /**
   * The camera preview size will be chosen to be the smallest frame by pixel size capable of
   * containing a DESIRED_SIZE x DESIRED_SIZE square.
   */
  private static final int MINIMUM_PREVIEW_SIZE = 320;

  /**
   * Conversion from screen rotation to JPEG orientation.
   */
  private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

  private static final String FRAGMENT_DIALOG = "dialog";

  static {
    ORIENTATIONS.append(Surface.ROTATION_0, 90);
    ORIENTATIONS.append(Surface.ROTATION_90, 0);
    ORIENTATIONS.append(Surface.ROTATION_180, 270);
    ORIENTATIONS.append(Surface.ROTATION_270, 180);
  }

  /**
   * A {@link Semaphore} to prevent the app from exiting before closing the camera.
   */
  private final Semaphore cameraOpenCloseLock = new Semaphore(1);
  /**
   * A {@link OnImageAvailableListener} to receive frames as they are available.
   */
  private final OnImageAvailableListener imageListener;
  /**
   * The input size in pixels desired by TensorFlow (width and height of a square bitmap).
   */
  private final Size inputSize;
  /**
   * The layout identifier to inflate for this Fragment.
   */
  private final int layout;

  private final ConnectionCallback cameraConnectionCallback;
  private final CameraCaptureSession.CaptureCallback captureCallback =
          new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureProgressed(
                    final CameraCaptureSession session,
                    final CaptureRequest request,
                    final CaptureResult partialResult) {
            }

            @Override
            public void onCaptureCompleted(
                    final CameraCaptureSession session,
                    final CaptureRequest request,
                    final TotalCaptureResult result) {
            }
          };
  /**
   * ID of the current {@link CameraDevice}.
   */
  private String cameraId;
  /**
   * An {@link AutoFitTextureView} for camera preview.
   */
  private AutoFitTextureView textureView;
  /**
   * A {@link CameraCaptureSession } for camera preview.
   */
  private CameraCaptureSession captureSession;
  /**
   * A reference to the opened {@link CameraDevice}.
   */
  private CameraDevice cameraDevice;
  /**
   * The rotation in degrees of the camera sensor from the display.
   */
  private Integer sensorOrientation;
  /**
   * The {@link Size} of camera preview.
   */
  private Size previewSize;
  /**
   * An additional thread for running tasks that shouldn't block the UI.
   */
  private HandlerThread backgroundThread;
  /**
   * A {@link Handler} for running tasks in the background.
   */
  private Handler backgroundHandler;
  /**
   * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a {@link
   * TextureView}.
   */
}