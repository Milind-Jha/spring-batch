package com.mavenark.transactions.util;

public class ChunkSizeUtil {

    public static int getChunkSize(int chunkSize){
        if(chunkSize<=0)
            chunkSize = 10;
        if(chunkSize>10000)
            chunkSize = 10000;
        return chunkSize;
    }
}
