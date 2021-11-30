package com.minkiapps.hos.test.util;

import ohos.app.Context;
import ohos.global.resource.*;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;

import java.io.IOException;
import java.util.Optional;

/**
 * Resources Utility
 */
public class ResUtil {

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.DEBUG, 0x0, ResUtil.class.getSimpleName());

    private ResUtil() {
    }

    /**
     * get the path from id.
     *
     * @param context the context
     * @param id      the id
     * @return the path from id
     */
    public static String getPathById(Context context, int id) {
        String path = "";
        if (context == null) {
            return path;
        }
        final ResourceManager manager = context.getResourceManager();
        if (manager == null) {
            return path;
        }
        try {
            path = manager.getMediaPath(id);
        } catch (IOException | NotExistException | WrongTypeException e) {
            HiLog.error(LABEL_LOG, "%{public}s: %{public}s", "getPathById", e.getLocalizedMessage());
        }
        return path;
    }

    public static PixelMap getPixelMap(Context context, int drawableId) {
        String drawingPath = getPathById(context, drawableId);
        ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
        srcOpts.formatHint = "image/jpg";
        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
        decodingOptions.desiredPixelFormat = PixelFormat.ARGB_8888;
        Resource assrt = null;
        try {
            assrt = context.getResourceManager().getRawFileEntry(drawingPath).openRawFile();
        } catch (IOException e) {
        }
        ImageSource source = ImageSource.create(assrt, srcOpts);
        PixelMap pixelMap = source.createPixelmap(decodingOptions);
        return pixelMap;
    }
}