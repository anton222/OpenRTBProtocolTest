package org.openrtb.test;

import com.google.openrtb.OpenRtb;
import com.google.openrtb.json.*;
import com.google.protobuf.CodedOutputStream;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ProtocolTest {
    public static void print(String s, Object... o) {
        System.out.println(String.format(s, o));
    }

    public static void main(String[] args) {
        try {
            String json_file = args[0]; // use file from first argument
            byte[] json_bytes = Files.readAllBytes(Paths.get(json_file));
            print("Sizes (bytes)");
            print("JSON: %d", json_bytes.length);
            String json = new String(json_bytes);
            OpenRtbJsonFactory openrtbJson = OpenRtbJsonFactory.create();
            OpenRtb.BidRequest r = openrtbJson.newReader().readBidRequest(json);
            // Gzip version
            ByteArrayOutputStream baosgz=new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baosgz);
            zos.putNextEntry(new ZipEntry("openrtb"));
            zos.write(json_bytes,0,json_bytes.length);
            zos.finish();
            byte[] jsongz = baosgz.toByteArray();
            print("JSON.gz: %d",jsongz.length);
            // Protobuf version
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            CodedOutputStream cout= CodedOutputStream.newInstance(baos);
            r.writeTo(cout);
            cout.flush();
            byte[] protobuf = baos.toByteArray();
            print("Protobuf: %d",protobuf.length);

            int max = 1000000;
            print("Speed per %d reads (ms)",max);
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < max; i++) {
                openrtbJson.newReader().readBidRequest(json);
            }
            print("JSON: %d",Math.round((int)((double) System.currentTimeMillis() - startTime)));
            startTime = System.currentTimeMillis();
            for (int i = 0; i < max; i++) {
                ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(jsongz));
                zis.getNextEntry();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                IOUtils.copy(zis,out);
                openrtbJson.newReader().readBidRequest(new String(out.toByteArray()));
            }
            print("JSON.gz: %d",Math.round((int)((double) System.currentTimeMillis() - startTime)));
            startTime = System.currentTimeMillis();
            for (int i = 0; i < max; i++) {
                OpenRtb.BidRequest.parseFrom(protobuf);
            }
            print("Protobuf: %d",Math.round((int)((double) System.currentTimeMillis() - startTime)));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
