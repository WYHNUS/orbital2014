package edu.nus.comp.dotagridandroid.appsupport;

import java.nio.*;
import java.util.ArrayList;
import java.io.*;

import edu.nus.comp.dotagridandroid.ui.renderers.BufferUtils;

public class ModelReader {
	private volatile boolean success = false;
	private Thread processTask;
	private FloatBuffer v, n, t;
	private InputStream src;
	public ModelReader(InputStream in) {
		this.src = in;
		processTask = new Thread() {
			@Override
			public void run() {
				processObjFile();
			}
		};
		processTask.start();
	}
	
	private void processObjFile() {
		BufferedInputStream in = new BufferedInputStream(src);
		ArrayList<float[]> vv = new ArrayList<>(), vt = new ArrayList<>(), vn = new ArrayList<>();
		ArrayList<int[]> f = new ArrayList<>();
		// only ascii
		try {
			byte[] buf = new byte[2048];
			int count = in.read(buf), i = 0;
			while (count > 0) {
				if (i == count) {
					count = in.read(buf);
					i = 0;
				}
				// skip chars other than space tab \r \n
				while (count > 0 && buf[i] == ' ' || buf[i] == '\r' || buf[i] == '\n' || buf[i] == '\t') {
					i++;
					if (i == count) {
						count = in.read(buf);
						i = 0;
					}
				}
				if (count == 0)
					break;
				switch (buf[i]) {
				case 'v':
					i++;
					if (i == count) {
						count = in.read(buf);
						i = 0;
					}
					if (count == 0)
						throw new Exception ("Unexpected end of file");
					switch (buf[i]) {
					case '\t':
					case ' ': {
						// vertex data - 3 required 1 optional
						while (count > 0 && buf[i] == ' ' || buf[i] == '\t') {
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						if (count == 0)
							throw new Exception ("Unexpected end of file, x component expected");
						else if ((buf[i] > '9' || buf[i] < '0') && buf[i] != '.')
							throw new Exception ("Invalid format, number expected for x component");
						float[] vertex = new float[4];
						vertex[3] = 1;
						vv.add(vertex);
						float num = 0;
						// x component
						while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
							num = num * 10 + buf[i] - '0';
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						vertex[0] = num;
						if (count == 0)
							throw new Exception ("Unexpected end of file, y component expected");
						else if (buf[i] == '.') {
							// fraction
							num = 0;
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
							int dec = 0;
							while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
								dec--;
								num = num * 10 + buf[i] - '0';
								i++;
								if (i == count) {
									count = in.read(buf);
									i = 0;
								}
							}
							vertex[0] += num * (float) Math.pow(10, dec);
							if (count == 0)
								throw new Exception ("Unexpected end of file, y component expected");
							else if (buf[i] != ' ' && buf[i] != '\t')
								throw new Exception ("Invalid format, y component expected");
						} else if (buf[i] != ' ' && buf[i] != '\t')
							throw new Exception ("Invalid format, y component expected");
						// y component
						while (count > 0 && buf[i] == ' ' || buf[i] == '\t') {
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						if (count == 0)
							throw new Exception ("Unexpected end of file, y component expected");
						else if ((buf[i] > '9' || buf[i] < '0') && buf[i] != '.')
							throw new Exception ("Invalid format, number expected for y component");
						num = 0;
						while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
							num = num * 10 + buf[i] - '0';
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						vertex[1] = num;
						if (count == 0)
							throw new Exception ("Unexpected end of file, z component expected");
						else if (buf[i] == '.') {
							// fraction
							num = 0;
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
							int dec = 0;
							while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
								dec--;
								num = num * 10 + buf[i] - '0';
								i++;
								if (i == count) {
									count = in.read(buf);
									i = 0;
								}
							}
							vertex[1] += num * (float) Math.pow(10, dec);
							if (count == 0)
								throw new Exception ("Unexpected end of file");
							else if (buf[i] != ' ' && buf[i] != '\t')
								throw new Exception ("Invalid format, z component expected");
						} else if (buf[i] != ' ' && buf[i] != '\t')
							throw new Exception ("Invalid format, z component expected");
						// z component
						while (count > 0 && buf[i] == ' ' || buf[i] == '\t') {
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						if (count == 0)
							throw new Exception ("Unexpected end of file, z component expected");
						else if ((buf[i] > '9' || buf[i] < '0') && buf[i] != '.')
							throw new Exception ("Invalid format, number expected for z component");
						num = 0;
						while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
							num = num * 10 + buf[i] - '0';
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						vertex[2] = num;
						if (count == 0 || buf[i] == '\n' || buf[i] == '\r')
							break;
						else if (buf[i] == '.') {
							// fraction
							num = 0;
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
							int dec = 0;
							while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
								dec--;
								num = num * 10 + buf[i] - '0';
								i++;
								if (i == count) {
									count = in.read(buf);
									i = 0;
								}
							}
							vertex[2] += num * (float) Math.pow(10, dec);
							if (count == 0 || buf[i] == '\n' || buf[i] == '\r')
								break;
							else if (buf[i] != ' ' && buf[i] != '\t' && buf[i] != '#')
								throw new Exception ("Invalid format, end of line expected");
						} else if (buf[i] != ' ' && buf[i] != '\t' && buf[i] != '#')
							throw new Exception ("Invalid format, w component expected");
						// w component (optional)
						while (count > 0 && buf[i] == ' ' || buf[i] == '\t') {
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						if (count == 0 || buf[i] == '\n' || buf[i] == '\r')
							break;
						else if (buf[i] == '#') {
							while (count > 0 && buf[i] != '\n' && buf[i] != '\r') {
								i++;
								if (i == count) {
									count = in.read(buf);
									i = 0;
								}
							}
							break;
						}
						else if ((buf[i] > '9' || buf[i] < '0') && buf[i] != '.')
							throw new Exception ("Invalid format, number expected for w component");
						num = 0;
						while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
							num = num * 10 + buf[i] - '0';
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						vertex[3] = num;
						if (count == 0)
							break;
						else if (buf[i] == '.') {
							// fraction
							num = 0;
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
							int dec = 0;
							while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
								dec--;
								num = num * 10 + buf[i] - '0';
								i++;
								if (i == count) {
									count = in.read(buf);
									i = 0;
								}
							}
							vertex[3] += num * (float) Math.pow(10, dec);
							if (count == 0)
								break;
							else if (buf[i] != ' ' && buf[i] != '\t' && buf[i] != '#' && buf[i] != '\n' && buf[i] != '\r')
								throw new Exception ("Invalid format, end of line expected");
						} else if (buf[i] != ' ' && buf[i] != '\t' && buf[i] != '#' && buf[i] != '\n' && buf[i] != '\r')
							throw new Exception ("Invalid format, end of line expected");
						while (count > 0 && buf[i] != '\n' && buf[i] != '\r') {
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						break;
					}
					case 't': {
						// texture - 2 required 1 optional
						while (count > 0 && buf[i] == ' ' || buf[i] == '\t') {
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						if (count == 0)
							throw new Exception ("Unexpected end of file, s component expected");
						else if ((buf[i] > '9' || buf[i] < '0') && buf[i] != '.')
							throw new Exception ("Invalid format, number expected for s component");
						float[] vertex = new float[4];
						vt.add(vertex);
						float num = 0;
						// s component
						while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
							num = num * 10 + buf[i] - '0';
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						vertex[0] = num;
						if (count == 0)
							throw new Exception ("Unexpected end of file");
						else if (buf[i] == '.') {
							// fraction
							num = 0;
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
							int dec = 0;
							while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
								dec--;
								num = num * 10 + buf[i] - '0';
								i++;
								if (i == count) {
									count = in.read(buf);
									i = 0;
								}
							}
							vertex[0] += num * (float) Math.pow(10, dec);
							if (count == 0)
								throw new Exception ("Unexpected end of file, t component expected");
							else if (buf[i] != ' ' && buf[i] != '\t')
								throw new Exception ("Invalid format, t component expected");
						} else if (buf[i] != ' ' && buf[i] != '\t')
							throw new Exception ("Invalid format, t component expected");
						// t component
						while (count > 0 && buf[i] == ' ' || buf[i] == '\t') {
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						if (count == 0)
							throw new Exception ("Unexpected end of file, z component expected");
						else if ((buf[i] > '9' || buf[i] < '0') && buf[i] != '.')
							throw new Exception ("Invalid format, number expected for z component");
						num = 0;
						while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
							num = num * 10 + buf[i] - '0';
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						vertex[1] = num;
						if (count == 0 || buf[i] == '\n' || buf[i] == '\r')
							break;
						else if (buf[i] == '.') {
							// fraction
							num = 0;
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
							int dec = 0;
							while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
								dec--;
								num = num * 10 + buf[i] - '0';
								i++;
								if (i == count) {
									count = in.read(buf);
									i = 0;
								}
							}
							vertex[1] += num * (float) Math.pow(10, dec);
							if (count == 0 || buf[i] == '\n' || buf[i] == '\r')
								break;
							else if (buf[i] != ' ' && buf[i] != '\t' && buf[i] != '#')
								throw new Exception ("Invalid format, end of line expected");
						} else if (buf[i] != ' ' && buf[i] != '\t' && buf[i] != '#')
							throw new Exception ("Invalid format, w component expected");
						// w component (optional)
						while (count > 0 && buf[i] == ' ' || buf[i] == '\t') {
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						if (count == 0 || buf[i] == '\n' || buf[i] == '\r')
							break;
						else if (buf[i] == '#') {
							while (count > 0 && buf[i] != '\n' && buf[i] != '\r') {
								i++;
								if (i == count) {
									count = in.read(buf);
									i = 0;
								}
							}
							break;
						}
						else if ((buf[i] > '9' || buf[i] < '0') && buf[i] != '.')
							throw new Exception ("Invalid format, number expected for w component");
						num = 0;
						while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
							num = num * 10 + buf[i] - '0';
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						vertex[2] = num;
						if (count == 0 || buf[i] == '\n' || buf[i] == '\r')
							break;
						else if (buf[i] == '.') {
							// fraction
							num = 0;
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
							int dec = 0;
							while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
								dec--;
								num = num * 10 + buf[i] - '0';
								i++;
								if (i == count) {
									count = in.read(buf);
									i = 0;
								}
							}
							vertex[2] += num * (float) Math.pow(10, dec);
							if (count == 0 || buf[i] == '\n' || buf[i] == '\r')
								break;
							else if (buf[i] != ' ' && buf[i] != '\t' && buf[i] != '#')
								throw new Exception ("Invalid format, end of line expected");
						} else if (buf[i] != ' ' && buf[i] != '\t' && buf[i] != '#')
							throw new Exception ("Invalid format, end of line expected");
						while (count > 0 && buf[i] != '\n' && buf[i] != '\r') {
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						break;
					}
					case 'n':
						// normal - 3 required
						while (count > 0 && buf[i] == ' ' || buf[i] == '\t') {
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						if (count == 0)
							throw new Exception ("Unexpected end of file, x component expected");
						else if ((buf[i] > '9' || buf[i] < '0') && buf[i] != '.')
							throw new Exception ("Invalid format, number expected for x component");
						float[] vertex = new float[3];
						vn.add(vertex);
						float num = 0;
						// x component
						while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
							num = num * 10 + buf[i] - '0';
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						vertex[0] = num;
						if (count == 0)
							throw new Exception ("Unexpected end of file, y component expected");
						else if (buf[i] == '.') {
							// fraction
							num = 0;
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
							int dec = 0;
							while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
								dec--;
								num = num * 10 + buf[i] - '0';
								i++;
								if (i == count) {
									count = in.read(buf);
									i = 0;
								}
							}
							vertex[0] += num * (float) Math.pow(10, dec);
							if (count == 0)
								throw new Exception ("Unexpected end of file, y component expected");
							else if (buf[i] != ' ' && buf[i] != '\t')
								throw new Exception ("Invalid format, y component expected");
						} else if (buf[i] != ' ' && buf[i] != '\t')
							throw new Exception ("Invalid format, y component expected");
						// y component
						while (count > 0 && buf[i] == ' ' || buf[i] == '\t') {
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						if (count == 0)
							throw new Exception ("Unexpected end of file, y component expected");
						else if ((buf[i] > '9' || buf[i] < '0') && buf[i] != '.')
							throw new Exception ("Invalid format, number expected for y component");
						num = 0;
						while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
							num = num * 10 + buf[i] - '0';
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						vertex[1] = num;
						if (count == 0)
							throw new Exception ("Unexpected end of file, z component expected");
						else if (buf[i] == '.') {
							// fraction
							num = 0;
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
							int dec = 0;
							while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
								dec--;
								num = num * 10 + buf[i] - '0';
								i++;
								if (i == count) {
									count = in.read(buf);
									i = 0;
								}
							}
							vertex[1] += num * (float) Math.pow(10, dec);
							if (count == 0)
								throw new Exception ("Unexpected end of file");
							else if (buf[i] != ' ' && buf[i] != '\t')
								throw new Exception ("Invalid format, z component expected");
						} else if (buf[i] != ' ' && buf[i] != '\t')
							throw new Exception ("Invalid format, z component expected");
						// z component
						while (count > 0 && buf[i] == ' ' || buf[i] == '\t') {
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						if (count == 0)
							throw new Exception ("Unexpected end of file, z component expected");
						else if ((buf[i] > '9' || buf[i] < '0') && buf[i] != '.')
							throw new Exception ("Invalid format, number expected for z component");
						num = 0;
						while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
							num = num * 10 + buf[i] - '0';
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						vertex[2] = num;
						if (count == 0 || buf[i] == '\n' || buf[i] == '\r')
							break;
						else if (buf[i] == '.') {
							// fraction
							num = 0;
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
							int dec = 0;
							while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
								dec--;
								num = num * 10 + buf[i] - '0';
								i++;
								if (i == count) {
									count = in.read(buf);
									i = 0;
								}
							}
							vertex[2] += num * (float) Math.pow(10, dec);
							if (count == 0 || buf[i] == '\n' || buf[i] == '\r')
								break;
							else if (buf[i] != ' ' && buf[i] != '\t' && buf[i] != '#')
								throw new Exception ("Invalid format, end of line expected");
						} else if (buf[i] != ' ' && buf[i] != '\t' && buf[i] != '#')
							throw new Exception ("Invalid format, end of line expected");
						while (count > 0 && buf[i] != '\n' && buf[i] != '\r') {
							i++;
							if (i == count) {
								count = in.read(buf);
								i = 0;
							}
						}
						break;
					case 'p':
						throw new Exception ("Parameter space vertices are not supported");
					default:
						throw new Exception ("Invalid format");
					}
					break;
				case 'f': {
					// face - exactly 3
					// v1
					while (count > 0 && buf[i] == ' ' || buf[i] == '\t') {
						i++;
						if (i == count) {
							count = in.read(buf);
							i = 0;
						}
					}
					if (count == 0)
						throw new Exception ("Unexpected end of file, v of v1 expected");
					else if ((buf[i] > '9' || buf[i] < '0') && buf[i] != '.')
						throw new Exception ("Invalid format, number expected for v of v1");
					int[] face = new int[9];
					f.add(face);
					int num = 0;
					while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
						num = num * 10 + buf[i] - '0';
						i++;
						if (i == count) {
							count = in.read(buf);
							i = 0;
						}
					}
					face[0] = num;
					if (count == 0)
						throw new Exception ("Unexpected end of file, vt of v1 expected");
					else if (buf[i] != '/')
						throw new Exception ("Invalid format, vt of v1 expected");
					i++;
					if (i == count) {
						count = in.read(buf);
						i = 0;
					}
					num = 0;
					while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
						num = num * 10 + buf[i] - '0';
						i++;
						if (i == count) {
							count = in.read(buf);
							i = 0;
						}
					}
					face[1] = num;
					if (count == 0)
						throw new Exception ("Unexpected end of file, vn of v1 expected");
					else if (buf[i] != '/')
						throw new Exception ("Invalid format, vn of v1 expected");
					i++;
					if (i == count) {
						count = in.read(buf);
						i = 0;
					}
					num = 0;
					while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
						num = num * 10 + buf[i] - '0';
						i++;
						if (i == count) {
							count = in.read(buf);
							i = 0;
						}
					}
					face[2] = num;
					if (count == 0)
						throw new Exception ("Unexpected end of file, v2 expected");
					else if (buf[i] != ' ' && buf[i] != '\t')
						throw new Exception ("Invalid format, v2 expected");
					// v2
					while (count > 0 && buf[i] == ' ' || buf[i] == '\t') {
						i++;
						if (i == count) {
							count = in.read(buf);
							i = 0;
						}
					}
					if (count == 0)
						throw new Exception ("Unexpected end of file, v of v2 expected");
					else if ((buf[i] > '9' || buf[i] < '0') && buf[i] != '.')
						throw new Exception ("Invalid format, number expected for v of v2");
					num = 0;
					while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
						num = num * 10 + buf[i] - '0';
						i++;
						if (i == count) {
							count = in.read(buf);
							i = 0;
						}
					}
					face[3] = num;
					if (count == 0)
						throw new Exception ("Unexpected end of file, vt of v2 expected");
					else if (buf[i] != '/')
						throw new Exception ("Invalid format, vt of v2 expected");
					i++;
					if (i == count) {
						count = in.read(buf);
						i = 0;
					}
					num = 0;
					while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
						num = num * 10 + buf[i] - '0';
						i++;
						if (i == count) {
							count = in.read(buf);
							i = 0;
						}
					}
					face[4] = num;
					if (count == 0)
						throw new Exception ("Unexpected end of file, vn of v2 expected");
					else if (buf[i] != '/')
						throw new Exception ("Invalid format, vn of v2 expected");
					i++;
					if (i == count) {
						count = in.read(buf);
						i = 0;
					}
					num = 0;
					while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
						num = num * 10 + buf[i] - '0';
						i++;
						if (i == count) {
							count = in.read(buf);
							i = 0;
						}
					}
					face[5] = num;
					if (count == 0)
						throw new Exception ("Unexpected end of file, v2 expected");
					else if (buf[i] != ' ' && buf[i] != '\t')
						throw new Exception ("Invalid format, v2 expected");
					// v3
					while (count > 0 && buf[i] == ' ' || buf[i] == '\t') {
						i++;
						if (i == count) {
							count = in.read(buf);
							i = 0;
						}
					}
					if (count == 0)
						throw new Exception ("Unexpected end of file, v of v3 expected");
					else if ((buf[i] > '9' || buf[i] < '0') && buf[i] != '.')
						throw new Exception ("Invalid format, number expected for v of v3");
					num = 0;
					while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
						num = num * 10 + buf[i] - '0';
						i++;
						if (i == count) {
							count = in.read(buf);
							i = 0;
						}
					}
					face[6] = num;
					if (count == 0)
						throw new Exception ("Unexpected end of file, vt of v3 expected");
					else if (buf[i] != '/')
						throw new Exception ("Invalid format, vt of v3 expected");
					i++;
					if (i == count) {
						count = in.read(buf);
						i = 0;
					}
					num = 0;
					while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
						num = num * 10 + buf[i] - '0';
						i++;
						if (i == count) {
							count = in.read(buf);
							i = 0;
						}
					}
					face[7] = num;
					if (count == 0)
						throw new Exception ("Unexpected end of file, vn of v3 expected");
					else if (buf[i] != '/')
						throw new Exception ("Invalid format, vn of v3 expected");
					i++;
					if (i == count) {
						count = in.read(buf);
						i = 0;
					}
					num = 0;
					while (count > 0 && buf[i] <= '9' && buf[i] >= '0') {
						num = num * 10 + buf[i] - '0';
						i++;
						if (i == count) {
							count = in.read(buf);
							i = 0;
						}
					}
					face[8] = num;
					if (count == 0 || buf[i] == '\n' || buf[i] == '\r')
						break;
					else if (buf[i] != ' ' && buf[i] != '\t' && buf[i] != '#')
						throw new Exception ("Invalid format");
					while (count > 0 && buf[i] != '\n' && buf[i] != '\r') {
						i++;
						if (i == count) {
							count = in.read(buf);
							i = 0;
						}
					}
					break;
				}
				case '#':
					// comment, fast forward to next line
					while (count > 0 && buf[i] != '\n' && buf[i] != '\r') {
						i++;
						if (i == count) {
							count = in.read(buf);
							i = 0;
						}
					}
					break;
				default:
					throw new Exception("Invalid format");
				}
			}
			FloatBuffer
				tv = BufferUtils.createFloatBuffer(f.size() * 12),
				tt = BufferUtils.createFloatBuffer(f.size() * 6),
				tn = BufferUtils.createFloatBuffer(f.size() * 12);
			for (i = 0, count = f.size(); i < count; i++) {
				final int[] idx = f.get(i);
				for (int j = 0; j < 3; j++) {
					tv.put(vv.get(idx[j * 3] - 1));
					tt.put(vt.get(idx[j * 3 + 1] - 1));
					tn.put(vn.get(idx[j * 3 + 2] - 1));
				}
			}
			// finalise
			v = tv;
			t = tt;
			n = tn;
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	}
	
	public FloatBuffer getVertexBuffer() {
		try {
			processTask.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (success)
			return v;
		else
			return null;
	}
	public FloatBuffer getNormalBuffer() {
		try {
			processTask.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (success)
			return n;
		else
			return null;
	}
	public FloatBuffer getTextureCoordinateBuffer() {
		try {
			processTask.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (success)
			return t;
		else
			return null;
	}
}
