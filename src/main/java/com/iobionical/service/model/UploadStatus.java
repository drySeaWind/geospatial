/*
 * 2018 Sami.
 */
package com.iobionical.service.model;

/**
 *
 * @author sami
 */
public class UploadStatus {

    private long uploaded;
    private long failed;

    public UploadStatus() {
        this.uploaded = 0;
        this.failed = 0;
    }

    public long getUploaded() {
        return this.uploaded;
    }

    public void incrementUploaded() {
        this.uploaded++;
    }

    public long getFailed() {
        return this.failed;
    }

    public void incrementFailed() {
        this.failed++;
    }

}
