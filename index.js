// import * as XMPP from 'stanza';
const XMPP = require( 'stanza' );
// **************** CONFIGURE THIS TO CHANGE THE NODES ************************ //
// Client
const cltaddress = 'alumchat.xyz';
// Neighbors and distances (connections)
const neighbors = {
  A: {B: 7},
  B: {A: 7, C: 1, D: 2},
  C: {B: 1},
  D: {B: 2},
};
// Neighbors
const neighborNames = {
  A: ['B'],
  B: ['A', 'B', 'C'],
  C: ['B'],
  D: ['B'],
};

// Create the client
const client = XMPP.createClient(
    {
      jid: 'echobot@DESKTOP-C8E0Q7A',
      password: 'hunter2',
    },
);

// Send the roster
client.on( 'session:started', () => {
  client.getRoster().then( _ => console.log( 'Got roster' ) );
  client.sendPresence();
} );

// Get info from arguments
const [, , clientLetter] = process.argv;

// Get all paths
let paths = neighbors[ clientLetter ];

// If no paths, client is using the wrong letter
if ( !paths ) throw new Error( 'Client path can only be A, B, C, or D' );

/**
 * Chooses the minimum from two numbers
 * @param one
 * @param two
 * @returns {number}
 */
function min( one, two ) {
  if ( one < two ) return one;
  return two;
}

// Get the initial shortest paths
const shortest_paths = neighbors[ clientLetter ];

// Set the shortest path of a key
function setPath( key, path1, path2 ) {
  shortest_paths[ key ] = min( path1, path2 );
}

// Convert to an array to make it possible to use Object Keys inside
function toArray( toBeArray ) {
  const propertyNames = Object.keys( toBeArray );
  let arr = [];
  for ( const name of propertyNames ) {
    arr.push( {name, value: toBeArray[ name ]} );
  }
  return arr;
}

// On chat, enter an immediate loop after setting the default values
client.on( 'chat', ( msg ) => {
  const received_shortest_paths = JSON.parse( msg.body );
  const shortest_path_array = toArray( received_shortest_paths );
  for ( const node of shortest_path_array ) {
    if ( !shortest_paths[ node.name ] ) {
      setPath( node.name, node.value, node.value );
      continue;
    }
    setPath( node.name, shortest_paths[ node.name ], node.value );
  }
  // The way this works is that it sends a JSON with the different values and always
  // Chooses the minimum between two nodes such as to find the shortest path  when a path
  // already exists
  client.sendMessage(
      {
        to: msg.from,
        body: JSON.stringify( shortest_paths ),
      } );
} );

// Send a message to all possible neighbors
for ( const neighbor of neighborNames[ clientLetter ] ) {
  client.sendMessage(
      {
        to: `${ neighbor }@${ cltaddress }`,
        body: JSON.stringify( shortest_paths ),
      } );
}
client.connect();
