// import * as XMPP from 'stanza';
const XMPP = require( 'stanza' );

const cltaddress = 'alumchat.xyz';
const neighbors = {

  A: {B: 7},
  B: {A: 7, C: 1, D: 2},
  C: {B: 1},
  D: {B: 2},
};

const neighborNames = {
  A: ['B'],
  B: ['A', 'B', 'C'],
  C: ['B'],
  D: ['B'],
};

const possible_neighbors = ['A', 'B', 'C', 'D'];

const client = XMPP.createClient(
    {
      jid: 'echobot@DESKTOP-C8E0Q7A',
      password: 'hunter2',
    },
);

client.on( 'session:started', () => {
  client.getRoster().then( _ => console.log( 'Got roster' ) );
  client.sendPresence();
} );

const [, , clientLetter] = process.argv;

let paths = neighbors[ clientLetter ];
if ( !paths ) throw new Error( 'Client path can only be A, B, C, or D' );

function min( one, two ) {
  if ( one < two ) return one;
  return two;
}

const shortest_paths = neighbors[ clientLetter ];

function setPath( key, path1, path2 ) {
  shortest_paths[ key ] = min( path1, path2 );
}

function toArray( toBeArray ) {
  const propertyNames = Object.keys( toBeArray );
  let arr = [];
  for ( const name of propertyNames ) {
    arr.push( {name, value: toBeArray[ name ]} );
  }
  return arr;
}

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

  client.sendMessage(
      {
        to: msg.from,
        body: JSON.stringify( shortest_paths ),
      } );
} );
for ( const neighbor of neighborNames[ clientLetter ] ) {
  client.sendMessage(
      {
        to: `${ neighbor }@${ cltaddress }`,
        body: JSON.stringify( shortest_paths ),
      } );
}
client.connect();
