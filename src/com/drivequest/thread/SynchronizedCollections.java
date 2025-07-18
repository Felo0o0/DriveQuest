/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.thread;

import com.drivequest.model.Vehicle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Clase que implementa colecciones sincronizadas para uso en entornos multihilo.
 * <p>
 * Proporciona metodos para crear y manipular colecciones seguras para
 * concurrencia, evitando condiciones de carrera y asegurando la consistencia.
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class SynchronizedCollections {
    
    /**
    * Constructor privado para evitar instanciación.
    */
    private SynchronizedCollections() {
    // Clase de utilidad, no debe instanciarse
    }
    
    // ==== LISTAS ====
    
    /**
    * Crea una lista sincronizada de vehiculos.
    * 
    * @return Lista sincronizada vacia
    */
    public static List<Vehicle> createSynchronizedList() {
    return Collections.synchronizedList(new ArrayList<>());
    }
    
    /**
    * Crea una lista sincronizada genérica.
    * 
    * @param <T> Tipo de elementos en la lista
    * @return Lista sincronizada vacía
    */
    public static <T> List<T> createSynchronizedList(Class<T> clazz) {
    return Collections.synchronizedList(new ArrayList<>());
    }
    
    /**
    * Convierte una lista normal a una lista sincronizada.
    * 
    * @param normalList Lista normal
    * @return Lista sincronizada con los mismos elementos
    */
    public static List<Vehicle> toSynchronizedList(List<Vehicle> normalList) {
    if (normalList == null) {
    return Collections.synchronizedList(new ArrayList<>());
    }
    return Collections.synchronizedList(new ArrayList<>(normalList));
    }
    
    /**
    * Convierte una lista normal a una lista sincronizada.
    * 
    * @param <T> Tipo de elementos en la lista
    * @param normalList Lista normal
    * @return Lista sincronizada con los mismos elementos
    */
    public static <T> List<T> toSynchronizedGenericList(List<T> normalList) {
    if (normalList == null) {
    return Collections.synchronizedList(new ArrayList<>());
    }
    return Collections.synchronizedList(new ArrayList<>(normalList));
    }
    
    /**
    * Crea una lista optimizada para lecturas concurrentes.
    * Es util cuando hay muchas lecturas y pocas escrituras.
    * 
    * @return Lista CopyOnWrite vacia
    */
    public static List<Vehicle> createCopyOnWriteList() {
    return new CopyOnWriteArrayList<>();
    }
    
    /**
    * Crea una lista CopyOnWrite genérica.
    * 
    * @param <T> Tipo de elementos en la lista
    * @return Lista CopyOnWrite vacía
    */
    public static <T> List<T> createCopyOnWriteList(Class<T> clazz) {
    return new CopyOnWriteArrayList<>();
    }
    
    /**
    * Convierte una lista normal a una lista CopyOnWrite.
    * 
    * @param <T> Tipo de elementos en la lista
    * @param normalList Lista normal
    * @return Lista CopyOnWrite con los mismos elementos
    */
    public static <T> List<T> toCopyOnWriteList(List<T> normalList) {
    if (normalList == null) {
    return new CopyOnWriteArrayList<>();
    }
    return new CopyOnWriteArrayList<>(normalList);
    }
    
    // ==== CONJUNTOS ====
    
    /**
    * Crea un conjunto sincronizado para almacenar patentes.
    * 
    * @return Conjunto concurrente vacio
    */
    public static Set<String> createLicensePlateSet() {
    return Collections.newSetFromMap(new ConcurrentHashMap<>());
    }
    
    /**
    * Crea un conjunto sincronizado genérico.
    * 
    * @param <T> Tipo de elementos en el conjunto
    * @return Conjunto sincronizado vacío
    */
    public static <T> Set<T> createSynchronizedSet(Class<T> clazz) {
    return Collections.synchronizedSet(new HashSet<>());
    }
    
    /**
    * Convierte un conjunto normal a un conjunto sincronizado.
    * 
    * @param <T> Tipo de elementos en el conjunto
    * @param normalSet Conjunto normal
    * @return Conjunto sincronizado con los mismos elementos
    */
    public static <T> Set<T> toSynchronizedSet(Set<T> normalSet) {
    if (normalSet == null) {
    return Collections.synchronizedSet(new HashSet<>());
    }
    return Collections.synchronizedSet(new HashSet<>(normalSet));
    }
    
    /**
    * Crea un conjunto CopyOnWrite.
    * 
    * @param <T> Tipo de elementos en el conjunto
    * @return Conjunto CopyOnWrite vacío
    */
    public static <T> Set<T> createCopyOnWriteSet(Class<T> clazz) {
    return new CopyOnWriteArraySet<>();
    }
    
    /**
    * Crea un conjunto ordenado concurrente.
    * 
    * @param <T> Tipo de elementos en el conjunto
    * @return Conjunto ordenado concurrente vacío
    */
    public static <T extends Comparable<? super T>> NavigableSet<T> createConcurrentSortedSet() {
    return new ConcurrentSkipListSet<>();
    }
    
    /**
    * Crea un conjunto ordenado concurrente con un comparador personalizado.
    * 
    * @param <T> Tipo de elementos en el conjunto
    * @param comparator Comparador para ordenar los elementos
    * @return Conjunto ordenado concurrente vacío
    */
    public static <T> NavigableSet<T> createConcurrentSortedSet(Comparator<? super T> comparator) {
    return new ConcurrentSkipListSet<>(comparator);
    }
    
    // ==== MAPAS ====
    
    /**
    * Crea un mapa sincronizado para validacion de patentes.
    * 
    * @return Mapa concurrente vacio
    */
    public static Map<String, Boolean> createLicensePlateMap() {
    return new ConcurrentHashMap<>();
    }
    
    /**
    * Crea un mapa concurrente genérico.
    * 
    * @param <K> Tipo de las claves
    * @param <V> Tipo de los valores
    * @return Mapa concurrente vacío
    */
    public static <K, V> ConcurrentMap<K, V> createConcurrentMap() {
    return new ConcurrentHashMap<>();
    }
    
    /**
    * Sincroniza operaciones sobre un mapa.
    * 
    * @param map Mapa a sincronizar
    * @param <K> Tipo de las claves
    * @param <V> Tipo de los valores
    * @return Mapa sincronizado
    */
    public static <K, V> Map<K, V> synchronizeMap(Map<K, V> map) {
    if (map == null) {
    return Collections.synchronizedMap(new HashMap<>());
    }
    return Collections.synchronizedMap(map);
    }
    
    /**
    * Convierte un mapa normal a un mapa concurrente.
    * 
    * @param <K> Tipo de las claves
    * @param <V> Tipo de los valores
    * @param normalMap Mapa normal
    * @return Mapa concurrente con los mismos elementos
    */
    public static <K, V> ConcurrentMap<K, V> toConcurrentMap(Map<K, V> normalMap) {
    if (normalMap == null) {
    return new ConcurrentHashMap<>();
    }
    return new ConcurrentHashMap<>(normalMap);
    }
    
    /**
    * Crea un mapa ordenado concurrente.
    * 
    * @param <K> Tipo de las claves
    * @param <V> Tipo de los valores
    * @return Mapa ordenado concurrente vacío
    */
    public static <K extends Comparable<? super K>, V> ConcurrentNavigableMap<K, V> createConcurrentSortedMap() {
    return new ConcurrentSkipListMap<>();
    }
    
    /**
    * Crea un mapa ordenado concurrente con un comparador personalizado.
    * 
    * @param <K> Tipo de las claves
    * @param <V> Tipo de los valores
    * @param comparator Comparador para ordenar las claves
    * @return Mapa ordenado concurrente vacío
    */
    public static <K, V> ConcurrentNavigableMap<K, V> createConcurrentSortedMap(Comparator<? super K> comparator) {
    return new ConcurrentSkipListMap<>(comparator);
    }
    
    /**
    * Sincroniza un mapa ordenado.
    * 
    * @param <K> Tipo de las claves
    * @param <V> Tipo de los valores
    * @param sortedMap Mapa ordenado a sincronizar
    * @return Mapa ordenado sincronizado
    */
    public static <K, V> SortedMap<K, V> synchronizeSortedMap(SortedMap<K, V> sortedMap) {
    if (sortedMap == null) {
    return Collections.synchronizedSortedMap(new TreeMap<>());
    }
    return Collections.synchronizedSortedMap(sortedMap);
    }
    
    // ==== COLAS ====
    
    /**
    * Crea una cola concurrente.
    * 
    * @param <T> Tipo de elementos en la cola
    * @return Cola concurrente vacía
    */
    public static <T> Queue<T> createConcurrentQueue() {
    return new ConcurrentLinkedQueue<>();
    }
    
    /**
    * Crea una cola bloqueante.
    * 
    * @param <T> Tipo de elementos en la cola
    * @param capacity Capacidad de la cola (0 para ilimitada)
    * @return Cola bloqueante vacía
    */
    public static <T> BlockingQueue<T> createBlockingQueue(int capacity) {
    return capacity > 0 ? new LinkedBlockingQueue<>(capacity) : new LinkedBlockingQueue<>();
    }
    
    /**
    * Sincroniza una cola.
    * 
    * @param <T> Tipo de elementos en la cola
    * @param queue Cola a sincronizar
    * @return Cola concurrente con los mismos elementos
    */
    public static <T> Queue<T> synchronizeQueue(Queue<T> queue) {
    if (queue == null) {
    return new ConcurrentLinkedQueue<>();
    }
    
    // Crear una nueva cola concurrente con los elementos de la cola original
    return new ConcurrentLinkedQueue<>(queue);
    }
    
    // ==== OPERACIONES ATÓMICAS ====
    
    /**
    * Añade un elemento a una lista de forma atómica.
    * 
    * @param <T> Tipo de elementos en la lista
    * @param list Lista sincronizada
    * @param element Elemento a añadir
    * @return true si se añadió el elemento
    */
    public static <T> boolean addAtomically(List<T> list, T element) {
    if (list == null || element == null) {
    return false;
    }
    
    synchronized (list) {
    return list.add(element);
    }
    }
    
    /**
    * Elimina un elemento de una lista de forma atómica.
    * 
    * @param <T> Tipo de elementos en la lista
    * @param list Lista sincronizada
    * @param element Elemento a eliminar
    * @return true si se eliminó el elemento
    */
    public static <T> boolean removeAtomically(List<T> list, T element) {
    if (list == null || element == null) {
    return false;
    }
    
    synchronized (list) {
    return list.remove(element);
    }
    }
    
    /**
    * Actualiza un elemento en un mapa de forma atómica.
    * 
    * @param <K> Tipo de las claves
    * @param <V> Tipo de los valores
    * @param map Mapa sincronizado
    * @param key Clave del elemento
    * @param value Nuevo valor
    * @return Valor anterior o null si no existía
    */
    public static <K, V> V updateAtomically(Map<K, V> map, K key, V value) {
    if (map == null || key == null) {
    return null;
    }
    
    synchronized (map) {
    return map.put(key, value);
    }
    }
    
    /**
    * Obtiene un elemento de un mapa de forma atómica, creándolo si no existe.
    * 
    * @param <K> Tipo de las claves
    * @param <V> Tipo de los valores
    * @param map Mapa concurrente
    * @param key Clave del elemento
    * @param defaultValueFunction Función para crear el valor por defecto
    * @return Valor existente o creado
    */
    public static <K, V> V getOrCreateAtomically(ConcurrentMap<K, V> map, K key, Function<K, V> defaultValueFunction) {
    if (map == null || key == null || defaultValueFunction == null) {
    return null;
    }
    
    return map.computeIfAbsent(key, defaultValueFunction);
    }
    
    // ==== COLECCIONES ESPECÍFICAS PARA VEHÍCULOS ====
    
    /**
    * Crea un mapa de vehículos indexado por patente.
    * 
    * @param vehicles Colección de vehículos
    * @return Mapa concurrente con vehículos indexados por patente
    */
    public static Map<String, Vehicle> createVehicleMap(Collection<Vehicle> vehicles) {
    if (vehicles == null) {
    return new ConcurrentHashMap<>();
    }
    
    return vehicles.stream()
    .filter(v -> v != null && v.getLicensePlate() != null)
    .collect(Collectors.toConcurrentMap(
    Vehicle::getLicensePlate,
    Function.identity(),
    (v1, v2) -> v1, // En caso de duplicados, mantener el primero
    ConcurrentHashMap::new
    ));
    }
    
    /**
    * Filtra vehículos de forma concurrente.
    * 
    * @param vehicles Colección de vehículos
    * @param filter Predicado para filtrar
    * @return Lista concurrente con los vehículos filtrados
    */
    public static List<Vehicle> filterVehicles(Collection<Vehicle> vehicles, Predicate<Vehicle> filter) {
    if (vehicles == null || filter == null) {
    return new CopyOnWriteArrayList<>();
    }
    
    return vehicles.parallelStream()
    .filter(filter)
    .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
    }
    
    /**
    * Agrupa vehículos por año de forma concurrente.
    * 
    * @param vehicles Colección de vehículos
    * @return Mapa concurrente con vehículos agrupados por año
    */
    public static Map<Integer, List<Vehicle>> groupVehiclesByYear(Collection<Vehicle> vehicles) {
    if (vehicles == null) {
    return new ConcurrentHashMap<>();
    }
    
    return vehicles.parallelStream()
    .filter(v -> v != null)
    .collect(Collectors.groupingByConcurrent(
    Vehicle::getYear,
    Collectors.toCollection(CopyOnWriteArrayList::new)
    ));
    }
}