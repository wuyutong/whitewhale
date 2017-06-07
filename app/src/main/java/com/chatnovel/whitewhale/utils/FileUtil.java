package com.chatnovel.whitewhale.utils;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

public class FileUtil {

    /**
     * Copy images to another path
     */
    @SuppressLint("SdCardPath")
    public static void copyFile(String oldPath, String newPath)
    {

        InputStream inStream;
        OutputStream jsOS = null;
        byte[] buffer = null;
        try
        {
            File oldfile = new File(oldPath);
            if (oldfile.exists())
            { // file not save

                inStream = new FileInputStream(oldPath); // read path
                BufferedInputStream bfn = new BufferedInputStream(inStream);
                buffer = new byte[bfn.available()];
                bfn.read(buffer);
                inStream.close();
                bfn.close();
                File outFile = new File(newPath);
                if (!outFile.getParentFile().exists())
                    outFile.getParentFile().mkdirs();
                jsOS = new FileOutputStream(outFile);
                jsOS.write(buffer, 0, buffer.length);
                jsOS.close();
            }

            inStream = null;
            jsOS = null;
            buffer = null;
        } catch (Exception e)
        {

            e.printStackTrace();
        } finally
        {
            inStream = null;
            jsOS = null;
            buffer = null;
        }
    }

    @SuppressWarnings("rawtypes")
    public static void unzip(String zipFilePath, String targetPath) throws IOException
    {
        OutputStream os = null;
        InputStream is = null;
        ZipFile zipFile = null;
        try
        {
            zipFile = new ZipFile(zipFilePath);
            String directoryPath = "";
            if (null == targetPath || "".equals(targetPath))
            {
                directoryPath = zipFilePath.substring(0, zipFilePath.lastIndexOf("."));
            } else
            {
                directoryPath = targetPath;
            }
            Enumeration entryEnum = zipFile.getEntries();
            if (null != entryEnum)
            {
                ZipEntry zipEntry = null;
                while (entryEnum.hasMoreElements())
                {
                    zipEntry = (ZipEntry) entryEnum.nextElement();
                    if (zipEntry.getSize() > 0)
                    {
                        File targetFile = buildFile(directoryPath + File.separator + zipEntry.getName());
                        os = new BufferedOutputStream(new FileOutputStream(targetFile));
                        is = zipFile.getInputStream(zipEntry);
                        byte[] buffer = new byte[4096];
                        int readLen = 0;
                        while ((readLen = is.read(buffer, 0, 4096)) >= 0)
                        {
                            os.write(buffer, 0, readLen);
                        }
                        os.flush();
                        os.close();
                    }
                }
            }
        } catch (IOException ex)
        {
            throw ex;
        } finally
        {
            if (null != zipFile)
            {
                zipFile = null;
            }
            if (null != is)
            {
                is.close();
            }
            if (null != os)
            {
                os.close();
            }
        }

    }

    @SuppressWarnings("rawtypes")
    public static void unzip(String filePath) throws IOException
    {
        OutputStream os = null;
        InputStream is = null;
        ZipFile zipFile = null;
        File srcFiles = new File(filePath);
        File files[] = srcFiles.listFiles();
        for (File file : files)
        {
            try
            {
                String zipFilePath = file.getAbsolutePath();
                zipFile = new ZipFile(zipFilePath);
                Enumeration entryEnum = zipFile.getEntries();
                if (null != entryEnum)
                {
                    ZipEntry zipEntry = null;
                    while (entryEnum.hasMoreElements())
                    {
                        zipEntry = (ZipEntry) entryEnum.nextElement();
                        if (zipEntry.getSize() > 0)
                        {
                            File targetFile = buildFile(filePath + File.separator + zipEntry.getName());
                            os = new BufferedOutputStream(new FileOutputStream(targetFile));
                            is = zipFile.getInputStream(zipEntry);
                            byte[] buffer = new byte[4096];
                            int readLen = 0;
                            while ((readLen = is.read(buffer, 0, 4096)) >= 0)
                            {
                                os.write(buffer, 0, readLen);
                            }
                            os.flush();
                            os.close();
                        }
                    }
                }
            } catch (IOException ex)
            {
                throw ex;
            } finally
            {
                if (null != zipFile)
                {
                    zipFile = null;
                }
                if (null != is)
                {
                    is.close();
                }
                if (null != os)
                {
                    os.close();
                }
            }
        }

    }

    public static File buildFile(String fileName)
    {
        File target = new File(fileName);
        if (!target.getParentFile().exists())
        {
            target.getParentFile().mkdirs();
            target = new File(target.getAbsolutePath());
        }
        return target;
    }

    public static JSONObject readJsonData(File cacheFile)
    {
        JSONObject jsonObject = null;
        if (cacheFile == null)
        {
            return jsonObject;
        }
        try
        {
            InputStream fIS = new FileInputStream(cacheFile);
            BufferedInputStream bin = new BufferedInputStream(fIS);
            byte bts[] = new byte[bin.available()];
            bin.read(bts);
            bin.close();
            fIS.close();
            jsonObject = new JSONObject(new String(bts, "UTF-8"));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        } catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
        return jsonObject;

    }


    /**
     * oldPath: 图片缓存的路径
     * newPath: 图片缓存copy的路径
     */
    public static void copyGlideFile(String oldPath, String newPath) {
        try {
            int byteRead;
            File oldFile = new File(oldPath);
            if (oldFile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ( (byteRead = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteRead);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static byte[] getFileByte(String path)
    {

        InputStream inStream;
        BufferedInputStream bfn = null;
        byte[] buffer = null;
        try
        {
            File file = new File(path);
            if (file != null)
            {

                inStream = new FileInputStream(file); // read path
                bfn = new BufferedInputStream(inStream);
                buffer = new byte[bfn.available()];
                bfn.read(buffer);
                bfn.close();
            }
            inStream = null;
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            inStream = null;
        }
        return buffer;
    }


    public static byte[] getFileByte(File file)
    {

        InputStream inStream;
        BufferedInputStream bfn = null;
        byte[] buffer = null;
        try
        {

            if (file != null)
            {

                inStream = new FileInputStream(file); // read path
                bfn = new BufferedInputStream(inStream);
                buffer = new byte[bfn.available()];
                bfn.read(buffer);
                bfn.close();
            }
            inStream = null;
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            inStream = null;
        }
        return buffer;
    }



    public static void saveMyBitmap(Bitmap mBitmap, String fileName)
    {
        File f = new File(fileName);
        FileOutputStream fOut = null;
        try
        {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try
        {
            fOut.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            fOut.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    // For to Delete the directory inside list of files and inner Directory
    public static boolean deleteAllWithPath(String path) {
        try {
            return FileUtil.deleteDir(new File(path));

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    // For to Delete the directory inside list of files and inner Directory
    private static boolean deleteDir(File dir) {
        if (dir == null) return false;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }




}
