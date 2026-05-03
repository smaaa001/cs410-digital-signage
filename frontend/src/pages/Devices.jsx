import { useEffect, useState } from 'react'
import '../styles/Devices.css'

function Devices() {
    const [devices, setDevices] = useState([]);
    const [groups, setGroups] = useState([]);

    useEffect(() => {
        console.log("get data from backend");
        //
        // setDevices(result from backend)
    }, [])


    return (
        <div className="devices-page">
            <h1>Device Management</h1>
            <h2>Manage displays and device groups</h2>

            <DeviceList devices={devices} setDevices={setDevices} />
            
            <DeviceGroups groups={groups} setGroups={setGroups} devices={devices} />

        </div>
    )
}

// Device Groups Section
function DeviceGroups({ groups, setGroups, devices }) {
    const [showModal, setShowModal] = useState(false);
    const [editingGroup, setEditingGroup] = useState(null);

    function handleDelete(id) {
        setGroups(groups.filter(group => group.id !== id));
    }

    function handleEdit(group) {
        setEditingGroup(group);
    }

    function handleEditSave(updatedGroup) {
        setGroups(groups.map(g => g.id === updatedGroup.id ? updatedGroup : g));
        setEditingGroup(null);
    }

    return (
        <div className="subpage">
            <div className="section-header">
                <h1>Device Groups</h1>
                <button onClick={() => setShowModal(true)}>New Group</button>
            </div>

            {groups.map(group => (
                <GroupCard key={group.id} group={group} onDelete={handleDelete} onEdit={handleEdit} />
            ))}

            {showModal && (
                <NewGroupModal
                    onClose={() => setShowModal(false)}
                    onAdd={(newGroup) => {
                        setGroups([...groups, newGroup]);
                        setShowModal(false);
                    }}
                    devices={devices}
                />
            )}

            {editingGroup && (
                <NewGroupModal
                    onClose={() => setEditingGroup(null)}
                    onAdd={handleEditSave}
                    devices={devices}
                    existingGroup={editingGroup}
                />
            )}
        </div>
    )
}

function GroupCard({ group, onDelete, onEdit }) {
    return (
        <div className="card">
            <div className="card-header">
                <h3>{group.name}</h3>
                <div className="card-UI">
                    <button onClick={() => onEdit(group)}>Edit</button>
                    <button onClick={() => onDelete(group.id)}>Delete</button>
                </div>
            </div>
            <p>{group.deviceIds.length} device{group.deviceIds.length !== 1 ? 's' : ''}</p>
        </div>
    )
}

// Devices Section
function DeviceList({ devices, setDevices }){
    const [showModal, setShowModal] = useState(false);
    const [editingDevice, setEditingDevice] = useState(null);
    
    function handleDelete(id) {
        setDevices(devices.filter(device => device.id !== id));
    }

    function handleEdit(device) {
        setEditingDevice(device);
    }

    function handleEditSave(updatedDevice) {
        setDevices(devices.map(d => d.id === updatedDevice.id ? updatedDevice : d));
        setEditingDevice(null);
    }
    
    return (
        <div className="subpage">
            <div className="section-header">
                <h1>Devices</h1>
                <button onClick={() => setShowModal(true)}>Add Device</button>
            </div>

            {devices.map(device => (<DeviceCard key={device.id} device={device} onDelete={handleDelete} onEdit={handleEdit}/>))}
            {showModal && (
                <NewDeviceModal
                    onClose={() => setShowModal(false)}
                    onAdd={(newDevice) => {
                        setDevices([...devices, newDevice]);
                        setShowModal(false);
                    }}
                />
            )}

            {editingDevice && (
                <NewDeviceModal
                    onClose={() => setEditingDevice(null)}
                    onAdd={handleEditSave}
                    existingDevice={editingDevice}
                />
            )}

        </div>
    )
}

function StatusBadge({ isOnline }) {
    const styles = {
        display: 'inline-flex',
        alignItems: 'center',
        gap: '6px',
        padding: '4px 12px',
        borderRadius: '6px',
        fontWeight: '600',
        fontSize: '0.85rem',
        color: 'white',
        backgroundColor: isOnline ? '#22c55e' : '#dc2626',
    };

    return (
        <span style={styles}>
            {isOnline ? '✓' : '✕'} {isOnline ? 'Online' : 'Offline'}
        </span>
    );
}

function DeviceCard({ device, onDelete, onEdit }) {
    return (
        <div className="card">
            <div className="card-header">
                <h3>{device.name}</h3>
                <div className="card-UI">
                    {/* <button>View</button> */}
                    <StatusBadge isOnline={device.isOnline} />
                    <button onClick={() => onEdit(device)}>Edit</button>
                    <button onClick={() => onDelete(device.id)}>Delete</button>
                </div>
            </div>
            <p>Pairing ID: {device.pairingId}</p>
        </div>
    );
}

function NewGroupModal({ onClose, onAdd, devices, existingGroup }) {
    const [name, setName] = useState(existingGroup?.name || '');
    const [selectedIds, setSelectedIds] = useState(existingGroup?.deviceIds || []);

    const availableDevices = devices.filter(d => !selectedIds.includes(d.id));
    const addedDevices = devices.filter(d => selectedIds.includes(d.id));

    function addDevice(id) {
        setSelectedIds(prev => [...prev, id]);
    }

    function removeDevice(id) {
        setSelectedIds(prev => prev.filter(d => d !== id));
    }

    function handleSubmit() {
        if (!name) return;
        onAdd({ id: existingGroup?.id ?? Date.now(), name, deviceIds: selectedIds });
    }

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal" onClick={e => e.stopPropagation()}>
                <h2>{existingGroup ? 'Edit Device Group' : 'New Device Group'}</h2>

                <label>Name</label>
                <input
                    value={name}
                    onChange={e => setName(e.target.value)}
                    placeholder="Group name"
                />

                <div style={{ display: 'flex', gap: '12px', marginTop: '8px' }}>
                    <div style={{ flex: 1 }}>
                        <label>Devices</label>
                        <div style={{ border: '1px solid #ccc', borderRadius: '6px', minHeight: '100px', padding: '6px', marginTop: '4px' }}>
                            {availableDevices.length === 0 ? (
                                <p style={{ color: '#aaa', fontSize: '0.85rem', margin: '4px' }}>No devices</p>
                            ) : (
                                availableDevices.map(device => (
                                    <div
                                        key={device.id}
                                        onClick={() => addDevice(device.id)}
                                        style={{ padding: '6px 8px', borderRadius: '4px', cursor: 'pointer', marginBottom: '4px', background: '#f5f5f5' }}
                                        onMouseEnter={e => e.currentTarget.style.background = '#e0e0e0'}
                                        onMouseLeave={e => e.currentTarget.style.background = '#f5f5f5'}
                                    >
                                        {device.name}
                                    </div>
                                ))
                            )}
                        </div>
                    </div>

                    <div style={{ flex: 1 }}>
                        <label>Added Devices</label>
                        <div style={{ border: '1px solid #ccc', borderRadius: '6px', minHeight: '100px', padding: '6px', marginTop: '4px' }}>
                            {addedDevices.length === 0 ? (
                                <p style={{ color: '#aaa', fontSize: '0.85rem', margin: '4px' }}>None added</p>
                            ) : (
                                addedDevices.map(device => (
                                    <div
                                        key={device.id}
                                        onClick={() => removeDevice(device.id)}
                                        style={{ padding: '6px 8px', borderRadius: '4px', cursor: 'pointer', marginBottom: '4px', background: '#e8f4e8' }}
                                        onMouseEnter={e => e.currentTarget.style.background = '#d0ebd0'}
                                        onMouseLeave={e => e.currentTarget.style.background = '#e8f4e8'}
                                    >
                                        {device.name}
                                    </div>
                                ))
                            )}
                        </div>
                    </div>
                </div>

                <div className="modal-buttons">
                    <button onClick={onClose}>Cancel</button>
                    <button onClick={handleSubmit}>{existingGroup ? 'Save' : 'Create'}</button>
                </div>
            </div>
        </div>
    );
}

function NewDeviceModal({ onClose, onAdd, existingDevice }) {
    const [name, setName] = useState(existingDevice?.name || '');
    const [pairingId, setPairingId] = useState(existingDevice?.pairingId || '');

    function handleSubmit() {
        if (!name || !pairingId) return;
        onAdd({ id: existingDevice?.id ?? Date.now(), name, pairingId });
    }

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal" onClick={e => e.stopPropagation()}>
                <h2>{existingDevice ? 'Edit Device' : 'Add Device'}</h2>
                <label>Device Name</label>

                <input
                    value={name}
                    onChange={e => setName(e.target.value)}
                    placeholder="Device name"
                />
                
                <label>Pairing ID</label>
                <input
                    value={pairingId}
                    onChange={e => setPairingId(e.target.value)}
                    placeholder="Pairing ID"
                />
                
                <div className="modal-buttons">
                    <button onClick={onClose}>Cancel</button>
                    <button onClick={handleSubmit}>{existingDevice ? 'Save' : 'Add'}</button>
                </div>
            </div>
        </div>
    );
}

export default Devices