package entities;

import javax.persistence.*;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Arrays;

@Entity
@Table(name = "files")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "fileName")
    private String fileName;
    @Column(name = "fileType")
    private String fileType;
    private Long size;
    @Lob
    @Column(name = "data", columnDefinition = "LONGBLOB")
    private byte[] data;

    public FileEntity() {
    }

    public FileEntity(String fileName, String fileType, Long size, byte[] data){
        this.fileName = fileName;
        this.fileType = fileType;
        this.size = size;
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", size=" + size +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
