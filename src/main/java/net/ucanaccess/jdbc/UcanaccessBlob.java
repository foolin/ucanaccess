/*
Copyright (c) 2012 Marco Amadei.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package net.ucanaccess.jdbc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

import com.healthmarketscience.jackcess.util.OleBlob;
import com.healthmarketscience.jackcess.util.OleBlob.Content;

public class UcanaccessBlob implements Blob {
    private Blob                 blob;
    private boolean              usingBlobKey;
    private UcanaccessConnection conn;

    public UcanaccessBlob(Blob _blob, UcanaccessConnection _conn) throws SQLException {
        this.blob = _blob;
        this.conn = _conn;
        if (_blob.length() != 0) {
            BlobKey bk = BlobKey.getBlobKey(_blob.getBinaryStream());
            this.usingBlobKey = (bk != null);
            if (this.usingBlobKey) {
                this.blob = bk.getOleBlob(this.conn.getDbIO());
            }
        }
    }

    public static Blob createBlob(File fl, UcanaccessConnection _conn) throws SQLException {
        Blob oleBlob;
        try {
            oleBlob = new OleBlob.Builder().setPackagePrettyName(fl.getName()).setSimplePackage(fl).toBlob();
            return new UcanaccessBlob(oleBlob, _conn);
        } catch (IOException e) {
            throw new UcanaccessSQLException(e);
        }
    }

    public static Blob createBlob(UcanaccessConnection _conn) throws SQLException {
        return new UcanaccessBlob(_conn.getHSQLDBConnection().createBlob(), _conn);
    }

    @Override
    public void free() throws SQLException {
        try {
            blob.free();
        } catch (SQLException e) {
            throw new UcanaccessSQLException(e);
        }
    }

    @Override
    public InputStream getBinaryStream() throws SQLException {
        try {
            if (this.usingBlobKey) {
                OleBlob ole = (OleBlob) this.blob;
                if (ole.getContent() instanceof OleBlob.EmbeddedContent) {
                    return ((OleBlob.EmbeddedContent) ole.getContent()).getStream();
                }
            }
            return blob.getBinaryStream();
        } catch (SQLException e) {
            throw new UcanaccessSQLException(e);
        } catch (IOException e) {
            throw new UcanaccessSQLException(e);
        }
    }

    @Override
    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        try {
            return blob.getBinaryStream(pos, length);
        } catch (SQLException e) {
            throw new UcanaccessSQLException(e);
        }
    }

    @Override
    public byte[] getBytes(long pos, int length) throws SQLException {
        try {
            return blob.getBytes(pos, length);
        } catch (SQLException e) {
            throw new UcanaccessSQLException(e);
        }
    }

    @Override
    public long length() throws SQLException {
        try {
            return blob.length();
        } catch (SQLException e) {
            throw new UcanaccessSQLException(e);
        }
    }

    @Override
    public long position(Blob pattern, long start) throws SQLException {
        try {
            return blob.position(pattern, start);
        } catch (SQLException e) {
            throw new UcanaccessSQLException(e);
        }
    }

    @Override
    public long position(byte[] pattern, long start) throws SQLException {
        try {
            return blob.position(pattern, start);
        } catch (SQLException e) {
            throw new UcanaccessSQLException(e);
        }
    }

    @Override
    public OutputStream setBinaryStream(long pos) throws SQLException {
        try {
            if(blob instanceof OleBlob && pos==1){
                OleBlob ole = (OleBlob) this.blob;
                Content content=ole.getContent();
                if (content instanceof OleBlob.EmbeddedContent){
                    ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                   ((OleBlob.SimplePackageContent)content).writeTo(baos);
                   return baos;
                } 
            }
            return blob.setBinaryStream(pos);
        } catch (SQLException e) {
            throw new UcanaccessSQLException(e);
        } catch (IOException e) {
            throw new UcanaccessSQLException(e);
        }
    }

    @Override
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        try {
            return blob.setBytes(pos, bytes);
        } catch (SQLException e) {
            throw new UcanaccessSQLException(e);
        }
    }

    @Override
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        try {
            return blob.setBytes(pos, bytes, offset, len);
        } catch (SQLException e) {
            throw new UcanaccessSQLException(e);
        }
    }

    @Override
    public void truncate(long len) throws SQLException {
        try {
            blob.truncate(len);
        } catch (SQLException e) {
            throw new UcanaccessSQLException(e);
        }
    }

}
