/*/////////////////////////////////////////////////////////////////
//
// Autor: Luiz Fernando Galati 
//                                                          
////////////////////////////////////////////////////////////////////*/



import java.math.BigInteger;
import java.util.Random;
import java.util.Arrays;

public class UniqueLRK
{
    private static int R = 256;
    private static long Q = longRandomPrime ();

    public static void main (String[] args)
    {        
        int L;
        
        L = Integer.parseInt (args[0]);

        if (args.length > 1) {
            int N = Integer.parseInt (args[1]);
            StdOut.printf ("%d\n", lAleatoria (N));
        }
        else {
            String input = StdIn.readAll ();
            if (L == 0)
                StdOut.printf ("%d\n", maiorL (input));
            else {
                int substrings = calculaSubstrings (input, L);
                StdOut.printf ("%d\n", substrings);
            }
        }
    }
    
    
    /* Devolve um número primo aleatório de 31 bits.                 */
    
    private static long longRandomPrime ()
    {
        BigInteger prime = BigInteger.probablePrime (31, new Random ());
        return prime.longValue ();
    }
    
    
    /* Devolve o valor do hash para a string key[0..M-1].            */
    
    private static long hash (String key, int M) 
    { 
        long h = 0;
        for (int j = 0; j < M; j++) 
            h = (R * h + key.charAt (j)) % Q; 
        return h; 
    } 

    
    /* Recebe uma string input - que contém um texto - e um inteiro L.
    Devolve o número de substrings únicas de comprimento L presentes
    no texto.                                                        */
    
    public static int calculaSubstrings (String input, int L)
    {
        int N;
        long RL, inputHash;
        RedBlackBST<Long, Integer> st;
        
        N = input.length ();
        st = new RedBlackBST<Long, Integer> ();       
        inputHash = hash (input, L);
        
        st.put (inputHash, 0);

        RL = 1;
        for (int i = 1; i <= L-1; i++)
            RL = (R * RL) % Q;       
        
        for (int i = L; i < N; i++) { 
            inputHash = (inputHash + Q - (RL)*input.charAt (i-L) % Q) % Q; 
            inputHash = (inputHash*R + input.charAt (i)) % Q;
            
            if (!st.contains (inputHash))
                st.put (inputHash, i - L + 1);
        }
        
        return st.size ();
    }
    

    /* Recebe uma string input, que contém um texto composto somente
    por dígitos numéricos. Devolve o maior valor de L para o qual a
    sequência de dígitos é L-completa.                               */

    public static int maiorL (String input)
    {        
        int N, L, i, maxL, maiorL;
        long inputHash, RL;
        String size;
        RedBlackBST<Long, Integer> st;
        
        N = input.length ();
        size = String.valueOf (N);
        maxL = size.length ();
        maiorL = 0;        
        
        for (L = 1; L < maxL; L++) {
            RL = 1;
            for (i = 1; i <= L - 1; i++)
                RL = (R * RL) % Q;
            
            st = new RedBlackBST<Long, Integer> ();
            inputHash = hash (input, L);
            st.put (inputHash, 0);
            for (i = L; i < N; i++) {
                inputHash = (inputHash + Q - RL*input.charAt (i-L) % Q) % Q; 
                inputHash = (inputHash*R + input.charAt (i)) % Q;               
                if (!st.contains (inputHash))
                    st.put (inputHash, i - L + 1);
                if (st.size () == Math.pow (10, L)) {
                    maiorL = L;
                    break;
                }
            }
            if (maiorL < L)
                break;
        }
        
        return maiorL;
    }
    

    /* Recebe um inteiro N. Gera uma sequência pseudo-aleatória de N 
    dígitos e devolve o maior valor de L para o qual a sequência é
    L-completa.                                                       */

    public static int lAleatoria (int N)
    {
        int i, lAleatoria;
        String nums;
        StringBuffer buffer;        
        
        buffer = new StringBuffer ();
        
        for (i = 0; i < N; i++) {
            int a = StdRandom.uniform (10);
            char c = (char) (a + 48);
            buffer.append (c);
        }
        
        nums = buffer.toString ();
        lAleatoria = maiorL (nums);
        
        return lAleatoria;
   }
}
