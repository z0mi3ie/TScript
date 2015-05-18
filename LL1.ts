//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Kyle Vickers
// TScript LL(1) Parser 
// CS712
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// VARIABLE DECLARATIONS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
var raw_input;
var trimmed_input;
var all_productions_array;
var all_nonterminals;
var all_terminals;
var null_deriving;


raw_input = readln();
all_productions_array = [];
all_nonterminals = [];
all_terminals = [];
null_deriving = [];


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// FILL ARRAY WITH THE PRODUCTIONS FROM FILE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
while(raw_input) {
  trimmed_input = trim(raw_input);
  all_productions_array.push( trimmed_input, all_productions_array);
  raw_input = readln();
}

//print all_productions_array;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// FIND THE START SYMBOL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
var splitRow;
split_row = split(all_productions_array[0]," ");

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// FIND ALL NON TERMINALS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
var i;
var j;
var contains_symbol;
var current_row;
var current_symbol;
var current_row_raw;
var current_row_array;
i = 0;
contains_symbol = false;

while( i < all_productions_array.size ) {
  j = 0;
  current_row_raw = all_productions_array[i]; 
  current_row_array = split(current_row_raw, " ");  
  //print current_row_array;
  current_symbol = current_row_array[0];
  //print current_symbol;
  // Check to see if current symbol is already in non terminals
  contains_symbol = false;
  while( j < all_nonterminals.size ) {
  //print all_nonterminals[j] + "==" + current_symbol;
    if( all_nonterminals[j] == current_symbol ) {
      contains_symbol = true;
    }
    j = j + 1;
  }
  if( contains_symbol == false ) {
    all_nonterminals.push(current_symbol,all_nonterminals);
  } 
  i = i + 1;
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// FIND ALL TERMINALS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
var i;
var j;
var k;
var contains_symbol;
var is_in_nonterminals;
var current_row;
var current_symbol;
var current_row_raw;
var current_row_array;
i = 0;

while( i < all_productions_array.size ) {
  current_row_raw = all_productions_array[i]; 
  current_row_array = split(current_row_raw, " ");  
  // Start after non terminal on current row
  j = 1;
  while( j < current_row_array.size ) {
    current_symbol = current_row_array[j];
    //print "current symbol";
    //print current_symbol;
    is_in_nonterminals = false;
    contains_symbol = false;
    k = 0;
    while( k < all_nonterminals.size ) {
      current_nonterminal = all_nonterminals[k];
      //print current_symbol + "==" + current_nonterminal;
      if( current_nonterminal == current_symbol ) {
        is_in_nonterminals = true;
        break;
      }
      k = k+1;
      // If current symbol is not in the nonterminals it is a terminal
    }


    l = 0;
    while( l < all_terminals.size ) {
    //print all_terminals[l] + "==" + current_symbol;
      if( all_terminals[l] == current_symbol ) {
        contains_symbol = true;
      }
      l = l + 1;
    }

    if( is_in_nonterminals == false ) {
      if( contains_symbol == false ) {
        all_terminals.push(current_symbol, all_terminals);
      }     
    }
    j = j+1;
  }

  i = i + 1;
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// FIND IMMEDIATELY NULL-DERIVING NON TERMINALS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
var a; 
var b;
var c;
var d; 
var current_symbol;
var current_row;
var current_row_raw;
var current_row_array;
var changes;
var already_added_to_null_deriving;

a = 0;
b = 0;
c = 0;
d = 0;

already_added_to_null_deriving = false;
changes = true;

while(changes) {
  //print "----- Start of Blank Pass -----";
  a = 0;
  b = 0;
  c = 0;
  changes = false;


  while( a < all_productions_array.size ) {
    already_added_to_null_deriving = false;
    current_row_raw = all_productions_array[a]; 
    current_row_array = split(current_row_raw, " ");  
    //print current_row_array;
    current_symbol = current_row_array[0];
  
    // -------------------------------------------------------------
    // If we are on a row that has no right side so we should try 
    // to add it to the null deriving
    // -------------------------------------------------------------

    if( current_row_array.size == 1 ) {
      b = 0;
      while( b < null_deriving.size ) {
        //print current_symbol + "==" + null_deriving[b];      
        if( current_symbol == null_deriving[b] ) {
          already_added_to_null_deriving = true;
        }
        b = b+1;
      }

      if(already_added_to_null_deriving == false) {
        null_deriving.push(current_symbol, null_deriving);
        changes = true;
      }
  } 
  a = a + 1;
  
  //print "Changes at end of outer loop: " + changes;
  }
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// FIND THE REST OF THE NULL-DERIVING NON TERMINALS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

a = 0;
b = 0;
c = 0;
d = 0;

already_added_to_null_deriving = false;
changes = true;

while(changes) {
  //print "----- Start of Pass Part 2 -----";
  a = 0;
  b = 0;
  c = 0;
  changes = false;

  while( a < all_productions_array.size ) {
    already_added_to_null_deriving = false;
    current_row_raw = all_productions_array[a]; 
    current_row_array = split(current_row_raw, " ");  
    current_symbol = current_row_array[0];
    //print "++++++++++++++++++++++++++++++++++++++++++++++";
    //print "Current LHS: " + current_symbol; 
    
    c = 1;
    var match_count;
    match_count = 0;
    while( c < current_row_array.size ) {
      current_row_symbol = current_row_array[c]; 
      //print "Current Row Symbol: " + current_row_symbol;
      d = 0;
      while( d < null_deriving.size ) {
        //print current_row_symbol + " -> " + null_deriving[d]; 
        if( current_row_symbol == null_deriving[d] ) {
          match_count = match_count + 1;
        }  
        d = d + 1;
      }  
      c = c + 1;
    }
    
    //print "MC / current_row_array.size: " + match_count + " " + current_row_array.size;
    var already_added;
    already_added = false;


    var rhs_size;
    rhs_size = current_row_array.size - 1;
    if( rhs_size == match_count ) {

      b = 0;
      already_added_to_null_deriving = false; 
      while( b < null_deriving.size ) {
        //print current_symbol + "==" + null_deriving[b];      
        if( current_symbol == null_deriving[b] ) {
          already_added_to_null_deriving = true;
        }
        b = b+1;
      }

      if(already_added_to_null_deriving == false) {
        null_deriving.push(current_symbol, null_deriving);
        changes = true;
      }

    }
    
    a = a + 1;
    //print "++++++++++++++++++++++++++++++++++++++++++++++";
  }  
  //print "Changes at end of outer loop: " + changes;
}


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// CALCULATE FIRST SETS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
var first_sets_key;
var first_sets_info;
var contains_symbol;
var current_row;
var current_symbol;
var current_row_raw;
var current_row_array;
var changes;
var a;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// FILL ARRAY WITH FIRST SET KEYS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//print "------------------------ FILLING FIRST SETS KEY ----------------------------";
a = 0;
first_sets_key = [];
first_sets_info = [];


a = 0;
while( a < all_productions_array.size ) {
  current_row_raw = all_productions_array[a]; 
  current_row_array = split(current_row_raw, " ");  
  current_symbol = current_row_array[0];
  var is_already_added;
  is_already_added = false;
  var b;
  b = 0;
  while( b < first_sets_key.size) {
    var current_set_symbol;
    current_set_symbol = first_sets_key[b];
    if( current_set_symbol == current_symbol ) {
      is_already_added = true;
    }
    b = b + 1;
  }
  
  if( is_already_added == false ) {
    first_sets_key.push(current_symbol,first_sets_key);
  }
  a = a + 1;
}
//print first_sets_key;
//print "----------------------------------------------------------------------------";



a = 0;
while( a < first_sets_key.size ) {
  var subarray;
  subarray = [];
  first_sets_info.push(subarray, first_sets_info);
  a = a + 1;
}



var current_rhs_symbol;
var is_a_terminal;
var is_a_nonterminal;
changes = true;
a = 0;
while( changes ) {
  //print "------------------------ START PASS ----------------------------";
  changes = false;
  a = 0;
  while( a < all_productions_array.size ) {
    //print "++++++++++++++++++++ ROW ++++++++++++++++++++";
    current_row_raw = all_productions_array[a]; 
    current_row_array = split(current_row_raw, " ");  
    //print current_row_array;
    current_symbol = current_row_array[0];
    //print "current_symbol: " + current_symbol; 
    a = a + 1;
    b = 1;
    while( b < current_row_array.size ) {
      current_rhs_symbol = current_row_array[b]; 
      //print "current_rhs_symbol: " + current_rhs_symbol;
      var c;
      c = 0;
      // ~~~~~~~~ CHECK TO SEE IF CURRENT RHS IS TERMINAL ~~~~~~~~~
      is_a_terminal = false;
      while( c < all_terminals.size ) {
        //print current_rhs_symbol + " == " + all_terminals[c];
        if( current_rhs_symbol == all_terminals[c] ) {
          is_a_terminal = true;
          break;
        }
        c = c + 1;
        is_a_terminal = false;
      } // leaving this loop if is_a_terminal
      //print current_rhs_symbol + " is a terminal: " + is_a_terminal;
      

      // ~~~~~~~~ CHECK TO SEE IF CURRENT RHS IS NON TERMINAL ~~~~~~~~~
      is_a_nonterminal = !is_a_terminal;
      //print current_rhs_symbol + " is a nonterminal: " + is_a_nonterminal;

      // ~~~~~~~~ CHECK TO SEE IF CURRENT NON TERMINAL NULL DERIVES ~~~
      is_null_deriving = false;
      if( is_a_nonterminal == true ) {
        c = 0;
        while( c < null_deriving.size ) {
          if( current_rhs_symbol == null_deriving[c] ) {
            is_null_deriving = true;
            break;
          }
          c = c + 1;
          is_null_deriving = false;
        }
      }
      //print current_rhs_symbol + " is null-deriving: " + is_null_deriving;
      
      // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   
      // ~~~~~~~ CURRENT SYMBOL IS A TERMINAL ~~~~~~~
      // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   
      if( is_a_terminal == true ) {
        // #### Find right index for first set and push this terminal onto it   
        var key_index;
        key_index = 0;
        while( key_index < first_sets_key.size ) {
          if( current_symbol == first_sets_key[key_index] ) {
            break;
          }
          key_index = key_index + 1;
        }
        //print "Key_Index: " + key_index; 
        c = 0;
        var already_added;
        already_added = false;
        var current_info;
        current_info = first_sets_info[key_index];
        while( c < current_info.size ) {
          if( current_info[c] == current_rhs_symbol ) {
            already_added = true;
          } 
          c = c + 1;
        }
        
        if( already_added == false ) {
          first_sets_info[key_index].push(current_rhs_symbol,first_sets_info[key_index]); 
          changes = true;
        }

        //print first_sets_info;
        break;
      } // Done adding to first_set_info

      // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   
      // ~~~~~~~ CURRENT SYMBOL IS A NON TERMINAL ~~~~~~~
      // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      if( is_a_nonterminal ) {
        var to_key_index;
        to_key_index = 0;
        while( to_key_index < first_sets_key.size ) {
          if( current_symbol == first_sets_key[to_key_index] ) {
            break;
          }
          to_key_index = to_key_index + 1;
        }

        var from_key_index;
        from_key_index = 0;
        while( from_key_index < first_sets_key.size ) {
          if( current_rhs_symbol == first_sets_key[from_key_index] ) {
            break;
          }
          from_key_index = from_key_index + 1;
        }
        
        //print "to_key_index: " + to_key_index; 
        //print "from_key_index: " + from_key_index; 

        var from_info;
        var to_info;
        var to_info_size_before;
        from_info = first_sets_info[from_key_index];
        to_info = first_sets_info[to_key_index]; 
        to_info_size_before = to_info.size;
      
        first_sets_info[to_key_index].join(from_info, first_sets_info[to_key_index]);
        
        var to_info_size_after;
        to_info = first_sets_info[to_key_index]; 
        to_info_size_after = to_info.size;
        
        //print "size before / size after " + to_info_size_before + " " + to_info_size_after;   

        if( to_info_size_before < to_info_size_after ) {
          changes = true;
        }
        
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   
        // ~~~~~~~ CURRENT SYMBOL IS A NON TERMINAL ~~~~~~~
        // ~~~~~~~ THAT DOESNT NULL DERIVE          ~~~~~~~
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if( is_null_deriving == false ) {
          break;
        }

        //print first_sets_info;
      } // End of symbol is a non terminal
      
      b = b + 1;
    } // End of current rhs symbols
    
    
  } // End of all_productions_loop
  //print "----------------------------------------------------------------";
}


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Part 1 - Start Symbol, Non-terminals, Terminals
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
print "Start Symbol\n";
print split_row[0];

print "\nNonterminals\n";
print all_nonterminals.stringify(all_nonterminals);

print "\nTerminals\n";
print all_terminals.stringify(all_terminals);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Part 2 - Null Deriving Nonterminals
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
print "\nNull-Deriving Nonterminals\n";
print null_deriving.stringify(null_deriving);


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Part 3 - First Sets
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
print "\nFirst Sets\n";
var first_sets_size;
first_sets_size = first_sets_key.size;
var i;
i = 0;
var current_line;
var x;
var so_far;
var and_finally;
while( i < first_sets_size ) {
  current_line = first_sets_key[i];
  x = first_sets_info[i].stringify(first_sets_info[i]);
  so_far = concatenate(current_line, ": ");
  and_finally = concatenate(so_far,x);
  print and_finally;
  i = i + 1;
}







