package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;
import org.openrdf.sail.memory.MemoryStore;
import org.openrdf.sail.nativerdf.NativeStore;


import Const.Const;

public class RepoUtil {
	private Repository repo;
	private MemoryStore memStore;
	private NativeStore natStore;
	private File repoFile;
	private RepositoryConnection repoConn;
	private UriUtil subjUri;
	private UriUtil predUri;
	private UriUtil objUri;
	private Value objLit;
	private PredicateUtil predUtil;
	private ValueFactory valueFactory;
	private LinkedHashModel model;
	
	/*
	 * To get the repository within memory.
	 */
	public RepoUtil() {
		repoFile = new File(Const.repoPath);
		memStore = new MemoryStore();
		repo = new SailRepository(memStore);
		initialize();
	}
	
	/*
	 * To get the repository on the disk.
	 */
	public RepoUtil(String repoPath) {
		repoFile = new File(repoPath);
		natStore = new NativeStore(repoFile);
		repo = new SailRepository(natStore);
		initialize();
	}
	
	/*
	 * To get the repository on the Http server.
	 */
	public RepoUtil(String server, String repoId) {
		repo = new HTTPRepository(server, repoId);
		initialize();
	}
	
	private void initialize() {
		subjUri = new UriUtil();
		predUri = new UriUtil();
		objUri = new UriUtil();
		predUtil = new PredicateUtil();
		valueFactory = new ValueFactoryImpl();
		model = new LinkedHashModel();
		try {
			repo.initialize();
		} catch(RepositoryException e) {
			
		}
	}

	public RepositoryConnection getConnection() {
		try {
			return repo.getConnection();
		} catch (RepositoryException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * To set the subject namespace
	 * and type.
	 */
	public void setSubjNsAndType(String ns, String type) {
		subjUri.setNameSpace(ns);
		subjUri.setType(type);
	}
	
	public void setNameSpace(String ns) {
		setNameSpace(ns,ns,ns);
	}
	
	public void setNameSpace(String subjNs, String predNs, String objNs) {
		subjUri.setNameSpace(subjNs);
		predUri.setNameSpace(predNs);
		objUri.setNameSpace(objNs);
	}
	
	public void setSubjType(String type) {
		subjUri.setType(type);
	}
	
	/*
	 * To set the predicate namespace
	 * and type.
	 */
	public void setPredNsAndType(String ns, String type) {
		predUri.setNameSpace(ns);
		predUri.setType(type);
	}
	
	public void setPredType(String type) {
		predUri.setType(type);
	}
	
	/*
	 * To set the object namespace
	 * and type.
	 */
	public void setObjNsAndType(String ns, String type) {
		objUri.setNameSpace(ns);
		objUri.setType(type);
	}
	
	public void setObjType(String type) {
		objUri.setType(type);
	}
	
	/*
	 * The URI-URI-Literal format SPO record.
	 */
	public void addRecord(URI subj, URI pred, Literal obj) {
		try {
			repoConn = repo.getConnection();
			repoConn.add(subj, pred, obj);
			repoConn.close();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} 
	}
	
	/*
	 * The URI-URI-URI format SPO record.
	 */
	public void addRecord(URI subj, URI pred, URI obj) {
		try {
			repoConn = repo.getConnection();
			repoConn.add(subj, pred, obj);
			repoConn.close();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * The Str-Str-Str format SPO record.
	 */
	public void addRecord(String subjStr, String predStr, String objStr, boolean uriFlag) {
		URI subj;
		URI pred;
		URI obj;
		Literal lit;
		try {
			repoConn = repo.getConnection();
//			repoConn.isActive()
			subj = subjUri.getUri(subjStr);
			if(predUtil.isDefUri(predStr)) {
				pred = predUtil.getDefUri(predStr);
			} else {
				pred = predUri.getUri(predStr);
			}
			
			if(uriFlag) {
				obj = objUri.getUri(objStr);
				repoConn.add(subj,pred,obj);
				model.add(valueFactory.createStatement(subj, pred, obj));
			} else {
				lit = valueFactory.createLiteral(objStr);
				repoConn.add(subj, pred,lit);
				model.add(valueFactory.createStatement(subj, pred, lit));
			}
			repoConn.close();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * The RDF reader for insert records
	 * with stream reader.
	 */
	public void addRecords(String rdfPath, String baseURI, RDFFormat format) {
		File rdfFile = new File(rdfPath);
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(rdfFile)));
			repoConn = repo.getConnection();
			repoConn.add(reader, baseURI, format);
			repoConn.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (RDFParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Read the RDF file with SPO format in stream.
	 */
	public void addRecords(String spoFilePath) {
//		File spoFile = new File(spoFilePath);
		InputStream in = null;
		try {
			in = new FileInputStream(spoFilePath);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		BufferedReader reader = null;
		String line = null;
		String[] segments = null;
		String subjStr, predStr, objStr;
		URI subj, pred;
		Literal lit = null;
		Statement stat = null;
//		ArrayList<Statement> model = new ArrayList<Statement>();
		LinkedHashModel model = new LinkedHashModel();
		
		System.out.println("Start reading file in line!");
		reader = new BufferedReader(new InputStreamReader(in));
		
		try {
			while( (line = reader.readLine()) != null) {
				if(line.length() <= 0) {
					continue;
				}
				//TODO
				segments = line.split("\t");
				subjStr = segments[0];
				predStr = segments[1];
				objStr = segments[2];
				ValueFactory vf = repo.getValueFactory();
				subj = vf.createURI(subjStr);
				pred = vf.createURI(predStr);
				lit = vf.createLiteral(objStr);
				stat = vf.createStatement(subj, pred, lit);
				model.add(stat);
			}
			reader.close();
			in.close();
			repoConn = repo.getConnection();
			repoConn.add(model);
			saveRDFTurtle(model, ".//test.n3", RDFFormat.N3);
			repoConn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Save triples into RDF files 
	 * with the specific RDF format.
	 */
	public void saveRDFTurtle(LinkedHashModel model, String filePath, RDFFormat rdfFormat) {
		try {
			FileOutputStream out = new FileOutputStream(filePath);
			RDFWriter writer = null;
			writer = Rio.createWriter(rdfFormat, out);
			writer.startRDF();
			for(Statement stat: model) {
				writer.handleStatement(stat);
			}
			writer.endRDF();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (RDFHandlerException e) {
			e.printStackTrace();
		}
		
	}
	
	public void saveRDFTurtle(String filePath, RDFFormat rdfFormat) {
		try {
			FileOutputStream out = new FileOutputStream(filePath);
			RDFWriter writer = null;
			writer = Rio.createWriter(rdfFormat, out);
			writer.startRDF();
			for(Statement stat: model) {
				writer.handleStatement(stat);
			}
			writer.endRDF();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (RDFHandlerException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * To save the triples in RDF turtle format
	 * directly from the Sesame database. 
	 * The third parameter is "self".
	 */
	public void saveRDFTurtle(String filePath, RDFFormat rdfFormat, String self) {
		try {
			FileOutputStream out = new FileOutputStream(filePath);
			RDFWriter writer = Rio.createWriter(rdfFormat, out);
			repoConn = repo.getConnection();
			repoConn.export(writer);
			repoConn.close();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (RDFHandlerException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
}
