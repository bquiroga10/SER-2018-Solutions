import java.util.*;
import java.math.*;
import java.io.*;

public class rectangles {
	class STXOR {
		int n, h;  byte d[];  long t[], len[];
		public STXOR(int nn, int[] lens) {
			t = new long[(n = nn) << 1];
			d = new byte[n];
			h = 32 - Integer.numberOfLeadingZeros(n);
			len = new long[n << 1];
			for(int i = 0 ; i < n ; i++) len[i + n] = lens[i];
			for(int i = n - 1 ; i > 0 ; i--) len[i] = len[i << 1] + len[i << 1 | 1]; 
		}
		void apply(int p, int v) {
			if(v == 1) t[p] = len[p] - t[p];
			if(p < n) d[p] ^= v;
		}
		void build(int p) {
			while(p > 1) {
				p = (p >> 1);
				if(d[p] == 1) t[p] = (len[p << 1] - t[p << 1]) + (len[p << 1 | 1] - t[p << 1 | 1]);
				else t[p] = t[p << 1] + t[p << 1 | 1];
			}
		}
		void push(int p) {
			for(int s = h ; s > 0 ; s--) {
				int i = (p >> s);
				if(d[i] != 0) {
					apply(i << 1, d[i]);
					apply(i << 1 | 1, d[i]);
					d[i] = 0;
				}
			}
		}
		void flip(int l, int r) { // flips [l, r]
			l += n;  r += n + 1;
			int l0 = l, r0 = r;
			for(; l < r ; l >>= 1, r >>= 1) {
				if((l & 1) > 0) apply(l++, 1);
				if((r & 1) > 0) apply(--r, 1);
			}
			build(l0);  build(r0 - 1);
		}
		long query(int l, int r) { // finds sum on [l, r]
			l += n;  r += n + 1;
			push(l);  push(r - 1);
			long res = 0;
			for(; l < r ; l >>= 1, r >>= 1) {
				if((l & 1) > 0) res += t[l++];
				if((r & 1) > 0) res += t[--r];
			}
			return res;
		}
	}
	class Event implements Comparable <Event> {
		int x1, x2, y, i;
		public Event(int xx1, int xx2, int yy, int ii) {
			x1 = xx1;  x2 = xx2;   y = yy;  i = ii;
		}
		public int compareTo(Event r) {
			if(y == r.y) return Integer.compare(i, r.i);
			return Integer.compare(y, r.y);
		}
	}
	public rectangles() {
		FS scan = new FS();
		PrintWriter out = new PrintWriter(System.out);
		int n = scan.nextInt();
		Map<Integer, Integer> ymap = new HashMap<>();
		TreeSet<Integer> xs = new TreeSet<>();
		Event[] e = new Event[n << 1];
		for(int i = 0 ; i < n ; i++) {
			int x1 = scan.nextInt(), y1 = scan.nextInt();
			int x2 = scan.nextInt(), y2 = scan.nextInt();
			xs.add(x1);  xs.add(x2);
			e[i] = new Event(x1, x2, y1, i);
			e[i + n] = new Event(x1, x2, y2, i);
		}
		TreeMap<Integer, Integer> map = new TreeMap<>();
		for(int x : xs) map.put(x, map.size());
		int ind = 0;
		int[] lens = new int[xs.size() - 1];
		for(int x : xs)
			if(xs.lower(x) != null)
				lens[ind++] = x - xs.lower(x);
		Arrays.sort(e);
		STXOR st = new STXOR(lens.length, lens);
		long prev = e[0].y;
		st.flip(map.get(e[0].x1), map.get(e[0].x2) - 1);
		long res = 0;
		for(int i = 1 ; i < e.length ; i++) {
			res += (e[i].y - prev) * st.query(0, lens.length - 1);
			prev = e[i].y;
			st.flip(map.get(e[i].x1), map.get(e[i].x2) - 1);
		}
		out.println(res);
		out.close();
	}
	class FS {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer("");
		public String next() {
			while(!st.hasMoreTokens()) {
				try { st = new StringTokenizer(br.readLine()); }
				catch(Exception e) { e.printStackTrace(); }
			}
			return st.nextToken();
		}
		public int nextInt() { return Integer.parseInt(next()); }
	}
	public static void main(String[] args) { new rectangles(); }
}
