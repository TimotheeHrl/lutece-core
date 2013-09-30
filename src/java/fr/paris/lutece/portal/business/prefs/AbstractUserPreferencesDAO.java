/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.portal.business.prefs;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.util.sql.DAOUtil;


/**
 * User Preferences DAO
 */
public abstract class AbstractUserPreferencesDAO implements IPreferencesDAO
{
    private final String _strSqlSelect = "SELECT pref_value FROM " + getPreferencesTable( )
            + " WHERE id_user = ? AND pref_key = ?";
    private final String _strSqlInsert = "INSERT INTO " + getPreferencesTable( )
            + " ( pref_value , id_user, pref_key ) VALUES ( ?, ?, ? ) ";
    private final String _strSqlUpdate = "UPDATE " + getPreferencesTable( )
            + " SET pref_value = ? WHERE id_user = ? AND pref_key = ?";
    private final String _strSqlDelete = "DELETE FROM " + getPreferencesTable( ) + " WHERE id_user = ? ";
    private final String _strSqlSelectAll = "SELECT pref_key FROM " + getPreferencesTable( ) + " WHERE id_user = ?";
    private final String _strSqlSelectByValue = "SELECT id_user FROM " + getPreferencesTable( )
            + " WHERE pref_key = ? AND pref_value = ? ";

    /**
     * Gets the preferences table
     * @return The table name that stores preferences
     */
    abstract String getPreferencesTable( );

    /**
     * {@inheritDoc }
     */
    @Override
    public String load( String strUserId, String strKey, String strDefault )
    {
        DAOUtil daoUtil = new DAOUtil( _strSqlSelect );
        daoUtil.setString( 1, strUserId );
        daoUtil.setString( 2, strKey );
        daoUtil.executeQuery( );

        String strValue = strDefault;

        if ( daoUtil.next( ) )
        {
            strValue = ( daoUtil.getString( 1 ) );
        }

        daoUtil.free( );

        return strValue;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> getUserId( String strKey, String strValue )
    {
        List<String> listUserId = new ArrayList<String>( );
        DAOUtil daoUtil = new DAOUtil( _strSqlSelectByValue );
        daoUtil.setString( 1, strKey );
        daoUtil.setString( 2, strValue );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            listUserId.add( daoUtil.getString( 1 ) );
        }

        daoUtil.free( );

        return listUserId;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( String strUserId, String strKey, String strValue )
    {
        String strSQL = _strSqlInsert;

        if ( load( strUserId, strKey, null ) != null )
        {
            strSQL = _strSqlUpdate;
        }

        DAOUtil daoUtil = new DAOUtil( strSQL );

        daoUtil.setString( 1, strValue );
        daoUtil.setString( 2, strUserId );
        daoUtil.setString( 3, strKey );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> keys( String strUserId )
    {
        List<String> list = new ArrayList<String>( );
        DAOUtil daoUtil = new DAOUtil( _strSqlSelectAll );
        daoUtil.setString( 1, strUserId );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            list.add( daoUtil.getString( 1 ) );
        }

        daoUtil.free( );

        return list;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void remove( String strUserId )
    {
        DAOUtil daoUtil = new DAOUtil( _strSqlDelete );
        daoUtil.setString( 1, strUserId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }
}
