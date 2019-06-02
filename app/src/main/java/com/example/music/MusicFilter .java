package com.example.music;

import java.io.File;
import java.io.FilenameFilter;


/*播放文件选择类*/
class MusicFilter implements FilenameFilter
{
    public boolean accept(File dir, String name)
    {
        /*指定扩展名类型*/
        return (name.endsWith(".mp3"));
    }
}