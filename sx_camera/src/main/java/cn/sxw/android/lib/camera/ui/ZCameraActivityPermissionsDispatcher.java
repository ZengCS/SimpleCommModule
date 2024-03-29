// This file was generated by PermissionsDispatcher. Do not modify!
package cn.sxw.android.lib.camera.ui;

import androidx.core.app.ActivityCompat;

import permissions.dispatcher.PermissionUtils;

final class ZCameraActivityPermissionsDispatcher {
  private static final int REQUEST_OPENCAMERA = 0;

  private static final String[] DEFAULT_PERMISSION = new String[] {"android.permission.RECORD_AUDIO","android.permission.CAMERA"};
  private static String[] PERMISSION_OPENCAMERA = new String[] {"android.permission.RECORD_AUDIO","android.permission.CAMERA"};

  private ZCameraActivityPermissionsDispatcher() {
  }

  static void openCameraWithPermissionCheck(ZCameraActivity target) {
    if(target.isOnlyCameraCapture()){
        PERMISSION_OPENCAMERA = new String[] {"android.permission.CAMERA"};
    }else {
      PERMISSION_OPENCAMERA = DEFAULT_PERMISSION;
    }

    if (PermissionUtils.hasSelfPermissions(target, PERMISSION_OPENCAMERA)) {
      target.openCamera();
    } else {
      ActivityCompat.requestPermissions(target, PERMISSION_OPENCAMERA, REQUEST_OPENCAMERA);
    }
  }

  static void onRequestPermissionsResult(ZCameraActivity target, int requestCode,
      int[] grantResults) {
    switch (requestCode) {
      case REQUEST_OPENCAMERA:
      if (PermissionUtils.verifyPermissions(grantResults)) {
        target.openCamera();
      } else {
        if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_OPENCAMERA)) {
          target.neverAskForCamera();
        } else {
          target.deniedForCamera();
        }
      }
      break;
      default:
      break;
    }
  }
}
