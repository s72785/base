/**
 * Copyright (C) 2014 Karlsruhe Institute of Technology
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package edu.kit.dama.mdm.content.mets;

import edu.kit.dama.authorization.entities.GroupId;
import edu.kit.dama.authorization.entities.IAuthorizationContext;
import edu.kit.dama.authorization.entities.Role;
import edu.kit.dama.authorization.entities.UserId;
import edu.kit.dama.authorization.entities.impl.AuthorizationContext;
import edu.kit.dama.authorization.exceptions.UnauthorizedAccessAttemptException;
import edu.kit.dama.commons.exceptions.ConfigurationException;
import edu.kit.dama.commons.exceptions.PropertyValidationException;
import edu.kit.dama.mdm.admin.UserGroup;
import edu.kit.dama.mdm.base.UserData;
import edu.kit.dama.mdm.base.UserQuota;
import edu.kit.dama.mdm.core.IMetaDataManager;
import edu.kit.dama.mdm.core.MetaDataManagement;
import edu.kit.dama.mdm.dataorganization.entity.core.ICollectionNode;
import edu.kit.dama.mdm.dataorganization.entity.core.IDataOrganizationNode;
import edu.kit.dama.mdm.dataorganization.entity.core.IFileNode;
import edu.kit.dama.mdm.dataorganization.entity.core.IFileTree;
import edu.kit.dama.mdm.dataorganization.impl.util.Util;
import edu.kit.dama.rest.staging.types.TransferTaskContainer;
import edu.kit.dama.staging.entities.ingest.IngestInformation;
import edu.kit.dama.staging.exceptions.StagingProcessorException;
import edu.kit.dama.staging.processor.AbstractStagingProcessor;
import edu.kit.dama.staging.services.impl.ingest.IngestInformationServiceLocal;
import edu.kit.dama.util.Constants;
import edu.kit.dama.util.FileUtils;
import edu.kit.lsdf.adalapi.AbstractFile;
import org.apache.commons.io.FileExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Properties;

//nichts aus UI nutzen
//import edu.kit.dama.ui.admin.utils.UIComponentTools;
//import edu.kit.dama.ui.commons.util.UIHelper;
//import edu.kit.dama.ui.commons.util.UIUtils7;

/**
 * Idea is to limit upload on criteria (recursive) file size
 *
 * @author s72785
 */
public class TryQuota extends AbstractStagingProcessor {

    /**
     * The logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TryQuota.class);

    /**
     * Default constructor.
     */
    public TryQuota(String pUniqueIdentifier) {
        super(pUniqueIdentifier);
        // kein Logger warnt vor nichts
//        LOGGER.error( "ERROR! Konstruktor " );
//        LOGGER.warn( "WARNING! Konstruktor ");
//        LOGGER.info( "INFORMATION! Konstruktor ");
//        LOGGER.debug( "DEBUG! Konstruktor ");
//        // aber das geht dann doch
//        System.out.println("TeST by vv01f: … Konstruktor" + 
//        		"\nI" + LOGGER.isInfoEnabled() + 
//        		"\nD" + LOGGER.isDebugEnabled() + 
//        		"\nT" + LOGGER.isTraceEnabled() + 
//        		"\nW" + LOGGER.isWarnEnabled() +
//        		"\n" 
//        		);
    }

	public String getName(){
		return "TryQuota";
	}

/* helper for filesize */
private static boolean isLink( File f ) {
    Path p = Paths.get( f.toString() );
    return Files.isSymbolicLink( p );
}
    private static long usedSpace( File path ) //throws FileExistsException
    {
        long size = 0l;
        if ( path == null ) {
            System.out.println( "ERROR: No Files in " + path );
            System.out.println("exists   :" + path.exists() );
            System.out.println("isDir    :" + path.isDirectory() );
            System.out.println("isFile   :" + path.isFile() );
            System.exit(1);
        }
        if ( isLink( path ) ) {
            return 0;
        }
        int c = 0;
        try {
            c = path.listFiles().length;
        } catch (Exception e) {
            System.out.println( "path : " + path );
            System.out.println( "link : " + isLink( path ) );
            System.out.println( "file : " + path.isFile() );
            System.out.println( "dir  : " + path.isDirectory() );
            System.out.println( "list : " + path.listFiles() );
            System.out.println( "count: " + c );
            e.printStackTrace();
        }
        if ( c == 0 ) {
            return 0;
        }
        for ( File file : path.listFiles()) {
            if ( file.isDirectory() ) {
                size += usedSpace(file);
            } else {
                try {
                    if ( isLink( file ) ) {
                        //+=0
                    } else {//file.isFile() …
                        //} else if(Files.isRegularFile(link)) {// had e.g. sockets and a pipe
                        //                System.out.println(file.getName() + " " + file.length());
                        size += file.length();
                    }
                } catch(NullPointerException e) {
                    System.out.println( file.toString()
                            + "\t" + e.getStackTrace());
                }
            }

        }
        return size;
    }
    /* suggestion instead using `AbstractFile.formatSize()` */
    public static String getHumanreadableSize(
            long size
            //, int base /* if you need it flexible */
    ) {
        //const int BASE10 = 1000; // 10^3
        //const int BASE2=1024; // 2^10
        int base = 1024;
        if(size <= 0) {
            return "0";
        }
        String[] units;
        switch(base){
            case 1000: //just in case it's needed for some reason
                units = new String[]{"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
                break;
            default:
                units = new String[]{
                        "B", "KiB", "MiB", "GiB", "TiB", "PiB" //, "EiB", "ZiB", "YiB"
                };
                break;
        }
        //}
        // logarithm laws say, we can eliminate the exponent by division
        // also in Java log10 ends up calling a native function
        int pot = (int) (Math.log10(size)/Math.log10(base));
        //mit long (< 2^63) ist Ende im Bereich Exbibyte (2^60 <= x < 2^70) schnell erreicht, nur innerhalb Pebibyte (2^50 <= x < 2^60) sichergestellt
        if( pot > units.length ) {
            System.out.println("");
        }
        return new DecimalFormat("#,##0.#").format( size/Math.pow( base, pot ) ) + " " + units[pot];
    }

    public static File getFileFromURLOrFileString( String fn ) {
        File f = new File( fn );
        URI u;
        int i = 0;
        if ( ! f.exists() ) {
            //tested only on GNU/Linux, will need alteration for others (such like Windows as we know people have tomcats running on Windows Servers)
            while ( fn.contains("file:") ) {
                i = fn.indexOf("file:");
                //System.out.println( " n: " + fn + " – " + i + " – " + ( (i >= 0) ? fn.substring(i) : "" ) );
                if ( i == 0 ) {
                    fn = fn.replaceFirst("file:","");
                } else {
                    fn = fn.substring(i);
                }
            }
            while ( fn.contains("//") ) {
                fn = fn.replaceAll("//","/");
            }
            //System.out.println( " n: " + fn + " – " + fn.indexOf("//") );
            u = (new File( fn )).toURI();
            f = new File( u );
        }
        return f;
    }
/* /helper for filesize */
    @Override
    public final void performPreTransferProcessing(TransferTaskContainer pContainer) throws StagingProcessorException {

        boolean preProcessingResult = true;
        Long IngestFileSize = 0L;
        Long QuotaMaxFileSize = 0L;
        Long QuotaMinFileSize = 1024L;

        // done: setup processor
        // done: run preprocessing
        // done: get context
        // done: find identifying user property for query
        // done: query Long userId, String distinguishedName etc. from db table `UserData`
        // done: use context/ingest as input for query
        // done: query Long quota from db table `UserQuota`
        // done: print as filesize
        // done: set IngestFileSize to used space by upload
        // done: compare ingest-size and quota
        // done: cause processor to fail
        // ?TODO: calculate available space for user
        // ?TODO: compare available vs needed space
        // ?TODO: delete data?
        // ?TODO: update table UserQuota for used space?

        IMetaDataManager mdm = MetaDataManagement.getMetaDataManagement().getMetaDataManager();

        // System.out.println("ctx = AuthorizationContect->SystemContext() …");
        IAuthorizationContext ctx = AuthorizationContext.factorySystemContext();
        mdm.setAuthorizationContext(ctx); // userID: SYS_ADMIN

        IngestInformation ingest = IngestInformationServiceLocal.getSingleton().getIngestInformationById(pContainer.getTransferId(), ctx);
        // ingest.getOwnerId() : admin

        /* find filesize */
        IngestFileSize = usedSpace(getFileFromURLOrFileString(pContainer.getDestination().toString()));
        System.out.println("Destination: " + pContainer.getDestination() );
        System.out.println("FileSize   : " + IngestFileSize + " (" + getHumanreadableSize( IngestFileSize ) + ")" );

        /* retrieving user data */
        //System.out.println("get UserData by String email");
        UserData userResult = null;
        try {
            userResult = mdm.findSingleResult(
                        "Select u FROM UserData u WHERE u.email=?1",
                        new Object[]{"dama@kit.edu"},
                        UserData.class
                );
        } catch (UnauthorizedAccessAttemptException e) {
            e.printStackTrace();
        }

        // System.out.println("get Long userId with String distinguishedName via ingest …");
        Long uid = null;
        try {
            uid = mdm.findSingleResult(
                    "SELECT u.userId FROM UserData u WHERE u.distinguishedName=?1",
                    new Object[]{ingest.getOwnerId()},
                    Long.class
            );
            // System.out.println("… Long userId: " + uid);
            // System.out.println("… .intValue(): " + uid.intValue() );
        } catch (UnauthorizedAccessAttemptException e) {
            e.printStackTrace();
        }

        /* get quota */
        // System.out.println("get UserQuota with Long uid …");
        UserQuota userQuota = null;
        try {
//            userQuota = mdm.findSingleResult("Select q FROM userquota WHERE q.uid=?1", new Object[]{"1"}, UserQuota.class );
            userQuota = mdm.findSingleResult(
                    "Select q FROM UserQuota q WHERE q.uid=?1",
                    new Object[]{uid},
                    UserQuota.class
            );
        } catch (UnauthorizedAccessAttemptException e2) {
            e2.printStackTrace();
        }
        if( userQuota == null ){
            System.out.println("userQuota is `null` -.-");
        } else {
            //System.out.println("print getQuota() …");
            try {
                Long quota = userQuota.getQuota();
                System.out.println("UserQuota  :  " + quota + " (" + AbstractFile.formatSize(quota) + ", " + AbstractFile.formatSize(quota)+")");
            } catch (Exception e4) {
                e4.printStackTrace();
            }
            QuotaMaxFileSize = userQuota.getQuota();
        }

//        System.out.println("get Number UserData.quota with Long uid …");
//        Number quota;
//        try {
//            quota = mdm.findSingleResult(
//                    "SELECT q.quota FROM UserQuota q WHERE q.uid=?1",
//                    new Object[]{uid},
//                    Number.class
//            );
//            if (quota == null){
//                System.out.println(" … Quota is `null`");
//            } else {
//                System.out.println(" … Number quota: " + quota);
//                System.out.println(" … as Filesize : " + AbstractFile.formatSize( (long) quota ) );
//
//            }
//        } catch (UnauthorizedAccessAttemptException e) {
//            e.printStackTrace();
//        }

//        QuotaMaxFileSize = quota.longValue();

        // simplified null-comparison might be error prone
        // TODO: maybe extend Number.class for isLowerThan(),isHigherThan()
        String Msg = "no aparent reason";
        if ( !preProcessingResult ) {
            preProcessingResult = (IngestFileSize != 0L)
                    && (QuotaMaxFileSize != 0L)
                    && (IngestFileSize <= QuotaMaxFileSize);
            if (!preProcessingResult) {
                Msg = "Quota Limit: maximum filesize (" + getHumanreadableSize(QuotaMaxFileSize) + ") exceeded: " + getHumanreadableSize(IngestFileSize) + "; Files " + getHumanreadableSize(IngestFileSize - QuotaMaxFileSize) + " too big.";
            }
        }
        if (preProcessingResult) {
            preProcessingResult = (IngestFileSize > 0L)
                    && (QuotaMinFileSize > 0L)
                    && (IngestFileSize >= QuotaMinFileSize);
            if (!preProcessingResult) {
                Msg = "Quota Limit: minimum filesize (" + getHumanreadableSize(QuotaMaxFileSize) + ") exceeded: " + getHumanreadableSize(IngestFileSize) + "; Files " + getHumanreadableSize(QuotaMinFileSize - IngestFileSize) + " too small.";
            }
        }

        // raise exception, so that Processor is not successful
        System.out.println("\nStagingProcessor TryQuota says: " + preProcessingResult + " .");
        if ( !preProcessingResult ) {
            System.out.println("\nraising exception … " + Msg);
            throw new StagingProcessorException("StagingProcessorException: " + Msg);
        }

    }

    @Override
    public final void finalizePreTransferProcessing(TransferTaskContainer pContainer) throws StagingProcessorException {

//        // hier auch ? kein Logger warnt vor nichts
//        LOGGER.error( "ERROR! finalizePreTransferProcessing " );
//        LOGGER.warn( "WARNING! finalizePreTransferProcessing ");
//        LOGGER.info( "INFORMATION! finalizePreTransferProcessing ");
//        LOGGER.debug( "DEBUG! finalizePreTransferProcessing ");
//        // aber das geht dann doch
//        System.out.println("TeST by vv01f: … finalizePreTransferProcessing" +
//        		"\nI" + LOGGER.isInfoEnabled() +
//        		"\nD" + LOGGER.isDebugEnabled() +
//        		"\nT" + LOGGER.isTraceEnabled() +
//        		"\nW" + LOGGER.isWarnEnabled() +
//        		"\n"
//        		);

    	// will not be called on server side!
//    	LOGGER.warn("WINFO: finalizePreTransferProcessing");
    }

	@Override
	public String[] getInternalPropertyKeys() {
		// Auto-generated method stub
    	LOGGER.warn("WINFO: getInternalPropertyKeys");
		return null;
	}

	@Override
	public String getInternalPropertyDescription(String pKey) {
		// Auto-generated method stub
    	LOGGER.warn("WINFO: getInternalPropertyDescription");
		return null;
	}

	@Override
	public String[] getUserPropertyKeys() {
		// Auto-generated method stub
    	LOGGER.warn("WINFO: getUserPropertyKeys");
		return null;
	}

	@Override
	public String getUserPropertyDescription(String pKey) {
		// Auto-generated method stub
    	LOGGER.warn("WINFO: getUserPropertyDescription");
		return null;
	}

	@Override
	public void validateProperties(Properties pProperties) throws PropertyValidationException {
		// Auto-generated method stub
    	LOGGER.warn("WINFO: validateProperties");
	}

	@Override
	public void configure(Properties pProperties) throws PropertyValidationException, ConfigurationException {
		// Auto-generated method stub
    	LOGGER.warn("WINFO: configure");		
	}

	@Override
	public void performPostTransferProcessing(TransferTaskContainer pContainer) throws StagingProcessorException {
//        System.out.println("TeST by vv01f: … performPostTransferProcessing" +
//        		"\nI" + LOGGER.isInfoEnabled() +
//        		"\nD" + LOGGER.isDebugEnabled() +
//        		"\nT" + LOGGER.isTraceEnabled() +
//        		"\nW" + LOGGER.isWarnEnabled() +
//        		"\n"
//        		);
		// Auto-generated method stub
    	LOGGER.warn("WINFO: performPostTransferProcessing");		
	}

	@Override
	public void finalizePostTransferProcessing(TransferTaskContainer pContainer) throws StagingProcessorException {
		// Auto-generated method stub
    	LOGGER.warn("WINFO: finalizePostTransferProcessing");		
	}

	public String[] getPropertyKeys(){
	    return new String[]{"metadataFilename"};
	}
}
